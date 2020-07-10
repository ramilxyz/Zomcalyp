package xyz.ramil.zomcalyp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;

import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import xyz.ramil.zomcalyp.ZGame;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		ZGame zgame = new ZGame();
		zgame.purchaseManager = new PurchaseManagerGoogleBilling(this);
		View gameView = initializeForView(zgame);
		FrameLayout frameLayout = findViewById(R.id.gameView);
		frameLayout.addView(gameView);
		final AdView adView = findViewById(R.id.adVidew);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		final Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
		adView.setAdListener(new AdListener(){
			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
				adView.startAnimation(animation);
			}
		});
	}
	}

