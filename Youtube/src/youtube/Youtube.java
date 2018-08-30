package youtube;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Youtube {

    private byte[] stdout;
    private Integer exitCode;

    public Youtube(String command) {
        try {
            ByteArrayOutputStream out;
            Process proc = Runtime.getRuntime().exec(command);

            out = new ByteArrayOutputStream();
            InputStream is = proc.getInputStream();
            OutputStream os = out;

            Thread t = new Thread() {
                public void run() {
                    PrintWriter pw = new PrintWriter(os);
                    InputStreamReader isr = null;
                    isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    try {
                        while ((line = br.readLine()) != null) {
                            pw.println(line);
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    pw.flush();
                }
            };

            this.exitCode = proc.waitFor();
            t.start();
            t.join();

            this.stdout = out.toByteArray();
            out.close();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public byte[] getStdout() {
        return stdout;
    }

    public Integer getExitCode() {
        return exitCode;
    }
}
