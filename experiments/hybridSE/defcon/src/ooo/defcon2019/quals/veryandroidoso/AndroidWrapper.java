package ooo.defcon2019.quals.veryandroidoso;

import android.os.Bundle;

public class AndroidWrapper {
	public void onCreate(Bundle bundle) {
		(new MainActivity()).onCreate(bundle);
	}
	
	public static void main(String[] args) {
		Bundle bd = new Bundle();
		(new MainActivity()).onCreate(bd);
	}
}
