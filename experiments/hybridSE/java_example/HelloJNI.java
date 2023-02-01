import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class HelloJNI {
	static {
        System.loadLibrary("hello");    // loads libhello.so
    }

    private native String addStr(String str);    

    private native int print(String name);
    
    private native int incr(int x, String pw);

	private native float findSqrt(int number);

	private native int calPerimeter(int a, int b, int c);

    private native String getHostName();
    private native int sockconnect(String arg);

    public double area(int a, int b, int c) {
    	int p;
    	double area = 0;
    	double s = 0;
		p = calPerimeter(a, b, c);
		if (p > 0) {
			s = p/2;
			area = Math.sqrt(s*(s-a)*(s-b)*(s-c));
		}
		return area;
	}

    public int foo(String s) {
	int len = s.length();
	int res = 0;
	if (len > 0) {
		s = addStr(s);
	}
	return s.length();
	}

    public int test(int x, int y) {
    	int tmp = 2*y;
    	if (x >= 0 && x > y && tmp > x+2) {
			x = incr(x, "messesage");
		}
		if (x + y < 10) {
			x = x + y;
		}
		return x;
		}

		public void sender() {
			try {
				String msg = "GET / HTTP/1.1\\r\\n\\r\\n";
				if (sockconnect(msg) == 0) {
					System.out.println("Message sent");
				} else {
					System.out.println("Connecion error");
				}

			} catch (Exception e) {
				System.out.println("Exception in native code");
			}
		}

//		

    public static void main(String[] args) {
		(new HelloJNI()).sender();
    	//(new HelloJNI()).area(4, 3, 5);
		//System.out.println((new HelloJNI()).foo("Hello mesg"));
    }
}


