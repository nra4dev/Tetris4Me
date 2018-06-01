package org.nrasoft.androidapp.mytetris.uc.game.components;

import org.nrasoft.androidapp.mytetris.uc.game.GameActivity;

public abstract class Component {

	protected GameActivity host;
	public GameActivity getHost() {
		return host;
	}



	public Component(GameActivity ga) {
		host = ga;
	}

	public void reconnect(GameActivity ga) {
		host = ga;
	}

	public void disconnect() {
		host = null;
	}


	
}
