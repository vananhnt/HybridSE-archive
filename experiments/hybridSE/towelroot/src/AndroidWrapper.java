
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import com.geohot.towelroot.TowelRoot;
import android.view.View; 
import com.geohot.towelroot.R;


public class  AndroidWrapper {
	public static void main(String[] args) {
		TowelRoot tw = new TowelRoot();
		//tw.onCreate(new Bundle());
		tw.rootTheShit();
	}
	
}