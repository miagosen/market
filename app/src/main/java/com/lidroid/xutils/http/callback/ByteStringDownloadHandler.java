/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.http.callback;

import com.lidroid.xutils.util.IOUtils;
import com.lx.market.network.serializer.MessageCodec;
import com.lx.market.network.utils.NetworkConstants;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteStringDownloadHandler {

  public byte[] handleEntity (HttpEntity entity, RequestCallBackHandler callBackHandler, String charset) throws IOException {
	if (entity == null) return null;
	byte[] result;
	long current = 0;
	long total = entity.getContentLength();

	if (callBackHandler != null && !callBackHandler.updateProgress(total, current, true)) {
	  return null;
	}

	InputStream inputStream = null;
	ByteArrayOutputStream saveStream = new ByteArrayOutputStream();
	try {
	  inputStream = entity.getContent();
	  byte[] recvBuffer = new byte[NetworkConstants.CONNECTION_BUFFER_SIZE];
	  int len = -1;
	  while ((len = inputStream.read(recvBuffer)) != -1) {
		saveStream.write(recvBuffer, 0, len);
		current += len;
		if (callBackHandler != null) {
		  if (!callBackHandler.updateProgress(total, current, false)) {
			break;
		  }
		}
	  }
	  if (callBackHandler != null) {
		callBackHandler.updateProgress(total, current, true);
	  }
	  byte[] responseBuff = saveStream.toByteArray();
	  result = MessageCodec.decryptResponse(responseBuff);
	} finally {
	  IOUtils.closeQuietly(inputStream);
	}
	return result;
  }

}
