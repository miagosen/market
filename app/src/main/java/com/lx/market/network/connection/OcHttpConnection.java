package com.lx.market.network.connection;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lx.market.MarketApplication;
import com.lx.market.network.callback.OcHttpReqCallBack;
import com.lx.market.network.model.RequestType;
import com.lx.market.network.model.ServerInfo;
import com.lx.market.network.model.Session;
import com.lx.market.network.protocol.GetServerReq;
import com.lx.market.network.protocol.GetServerResp;
import com.lx.market.network.serializer.AttributeUitl;
import com.lx.market.network.serializer.MessageCodec;
import com.lx.market.network.serializer.OcComMessageHead;
import com.lx.market.network.serializer.OcHttpMessage;
import com.lx.market.network.serializer.OcResponseResultCode;
import com.lx.market.network.serializer.SignalCode;
import com.lx.market.network.utils.NetworkConstants;
import com.lx.market.network.utils.TerminalInfoUtil;
import com.lx.market.utils.EncryptUtils;
import com.lx.market.utils.HandlerConstants;
import com.lx.market.utils.Logger;

import org.apache.http.entity.ByteArrayEntity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Antikvo.Miao on 2014/8/1.
 */
public class OcHttpConnection {
  private Session session;
  private RequestType reqType;
  private Object req;
  private Class<? extends OcResponseResultCode> respClass;
  private OcHttpReqCallBack callback;
  private Handler mHandler;
  private Context mContext;
  private int retryCount = 0;

  public OcHttpConnection (Context mContext) {
	session = MarketApplication.getInstance().getSession();
	this.mContext = mContext;
	mHandler = new Handler(mContext.getMainLooper()) {
	  @Override
	  public void handleMessage (Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		  case HandlerConstants.HANDLER_SUCCESS:
			sendReq(session.getServerAddress(reqType), req, respClass, callback);
			break;
		  case HandlerConstants.HANDLER_ERROR:
			callback.onResponse(false, msg.obj);
			break;
		}
	  }
	};

  }

  private boolean isNeedUpdateSession () {
	if (session == null || session.isEmpty() || Math.abs(session.getTime() - System.currentTimeMillis()) > 24 * 60 * 60 * 1000) {
	  return true;
	}
	return false;
  }

  private static OcHttpMessage getMessage (Object requestObject) throws Exception {
	SignalCode code = AttributeUitl.getMessageAttribute(requestObject);
	if (code == null || code.messageCode() == 0) {
	  throw new Exception("can't get message code");
	}
	UUID uuid = UUID.randomUUID();
	OcHttpMessage message = new OcHttpMessage();
	message.head = new OcComMessageHead();
	message.head.setVersion(NetworkConstants.PROTOCL_VERSION);
	message.head.setType(NetworkConstants.PROTOCL_REQ);
	message.head.setLeastSignificantBits(uuid.getLeastSignificantBits());
	message.head.setMostSignificantBits(uuid.getMostSignificantBits());
	message.head.setMessageCode(code.messageCode());
	message.body = requestObject;
	return message;
  }

  public synchronized <T extends OcResponseResultCode> void sendRequest (RequestType reqType, Object req, Class<T> t, OcHttpReqCallBack callback) {
	this.reqType = reqType;
	this.req = req;
	this.callback = callback;
	this.respClass = t;
	if (!isNeedUpdateSession()) {
	  sendReq(session.getServerAddress(reqType), req, respClass, this.callback);
	} else {
	  reqServerAddress(retryCount, serverAddrCallback);
	}
  }

  private void reqServerAddress (int retryCount, OcHttpReqCallBack serverAddrCallback) {
	GetServerReq serverReq = new GetServerReq();
	serverReq.setTerminalInfo(TerminalInfoUtil.getTerminalInfoForZone(mContext));
	serverReq.setSource("market");
	Logger.error(NetworkConstants.TAG, req.toString());
	String hostAddr = getServerAddr(retryCount);
	sendReq(hostAddr, serverReq, GetServerResp.class, serverAddrCallback);
  }

  private OcHttpReqCallBack serverAddrCallback = new OcHttpReqCallBack() {
	@Override
	public void onResponse (boolean result, Object respOrMsg) {
	  if (result) {
		GetServerResp serverAddrResp = (GetServerResp) respOrMsg;
		session = saveNetwrokAdd(serverAddrResp.getServerList());
		mHandler.sendEmptyMessage(HandlerConstants.HANDLER_SUCCESS);
	  } else {
		if (retryCount < 3) {
		  retryCount++;
		  reqServerAddress(retryCount, serverAddrCallback);
		} else {
		  Message msg = new Message();
		  msg.what = HandlerConstants.HANDLER_ERROR;
		  msg.obj = respOrMsg;
		}
	  }
	}
  };

  private String getServerAddr (int retryCount) {
	String addr = EncryptUtils.getOcNetworkAddr(retryCount);
	if (!addr.startsWith("http")) {
	  addr = "http://" + addr;
	}
	return addr;
  }

  private Session saveNetwrokAdd (List<ServerInfo> arrayList) {
	Session si = new Session();
	si.setId(1);//默认设置数据就一条数据
	for (ServerInfo bto : arrayList) {
	  // 1: 市场；2：数据统计;3：自更新
	  Logger.debug(NetworkConstants.TAG, bto.getIp() + ":" + bto.getPort() + "--" + bto.getModuleId());
	  if (bto.getModuleId() == 1) {
		si.setMarketNetworkAddr(bto.getIp() + ":" + bto.getPort());
	  } else if (bto.getModuleId() == 2) {
		si.setStatisNetworkAddr(bto.getIp() + ":" + bto.getPort());
	  } else if (bto.getModuleId() == 3) {
		si.setUpdateNetworkAddr(bto.getIp() + ":" + bto.getPort());
	  }
	}
	try {
	  MarketApplication.getInstance().setSession(si);
	  MarketApplication.getInstance().dbUtils.deleteAll(Session.class);
	  MarketApplication.getInstance().dbUtils.save(si);
	} catch (DbException e) {
	  Logger.p(e);
	}
	return si;
  }

  private <T extends OcResponseResultCode> void sendReq (String addr, Object req, final Class<T> t, final OcHttpReqCallBack callback) {
	Logger.d(NetworkConstants.TAG, "request url = " + addr);
	RequestParams params = new RequestParams();
	try {
	  SignalCode signalCode = AttributeUitl.getMessageAttribute(req);
	  boolean isCompress = signalCode.compress();
	  if (isCompress) {
		params.addHeader("isPress", "true");
	  }
	  params.setBodyEntity(new ByteArrayEntity(MessageCodec.serializeMessage(getMessage(req))));
	} catch (Exception e) {
	  e.printStackTrace();
	}
	HttpUtils httpUtils = new HttpUtils();
	httpUtils.send(HttpRequest.HttpMethod.POST, addr, params, new RequestCallBack<byte[]>() {
	  @Override
	  public void onSuccess (ResponseInfo<byte[]> responseInfo) {
		byte[] result = responseInfo.result;
		if (result == null || result.length == 0) {
		  callback.onResponse(false, "deserializeMessage error:result is null");
		} else {
		  SignalCode signalCode = AttributeUitl.getMessageAttribute(t);
		  boolean isCompress = false;
		  if (signalCode != null) {
			isCompress = signalCode.compress();
		  }
		  OcHttpMessage respMessage = MessageCodec.deserializeMessage(result, isCompress);
		  if (respMessage != null) {
			try {
			  respMessage.head = MessageCodec.deserializeHead(respMessage.getHeadJson());
			  respMessage.body = MessageCodec.deserializeBody(respMessage.getBodyJson(), t);
			  int respMsgCode = respMessage.head.getMessageCode();
			  int code = AttributeUitl.getMessageCode(t);
			  if (respMsgCode != code) {
				callback.onResponse(false, "deserializeMessage error: respMsgCode error");
			  }
			  if (respMessage.head != null && respMessage.body != null) {
				callback.onResponse(true, respMessage.body);
			  } else {
				callback.onResponse(false, "deserializeMessage error: head or body is null");
			  }
			} catch (Exception e) {
			  callback.onResponse(false, "deserializeMessage error:" + e.getMessage());
			  Logger.p(e);
			}
		  }else{
			  callback.onResponse(false, "respMessage is null: deserializeMessage error" );
		  }
		}
	  }

	  @Override
	  public void onFailure (HttpException error, String msg) {
		Logger.e(NetworkConstants.TAG, "onFailure:" + msg);
		callback.onResponse(false, msg);
	  }
	});
  }
}
