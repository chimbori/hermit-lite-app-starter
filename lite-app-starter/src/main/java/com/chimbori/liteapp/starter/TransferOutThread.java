package com.chimbori.liteapp.starter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Thread used to copy the InputStream contents to an OutputStream on the pipe, to transfer that
 * data to the client of this provider.
 * <p>
 * Copied from https://github.com/commonsguy/cwac-provider
 * http://www.apache.org/licenses/LICENSE-2.0
 */
class TransferOutThread extends Thread {
  private static final int BUFFER_SIZE = 8 * 1024;

  private InputStream in;
  private OutputStream out;
  private byte[] buffer = new byte[BUFFER_SIZE];

  TransferOutThread(InputStream in, OutputStream out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void run() {
    int len;
    try {
      while ((len = in.read(buffer)) >= 0) {
        out.write(buffer, 0, len);
      }
      in.close();
      out.close();

    } catch (IOException e) {
      // TODO: Report this more elegantly.
      e.printStackTrace();
    }
  }
}
