package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	ParticleEffect prototype;

	ParticleEffectPool pool;
	Array<ParticleEffectPool.PooledEffect> effects;

	final int MAX_POOL_SIZE = 300;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.input.setInputProcessor(new InputListener() {
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				ParticleEffectPool.PooledEffect effect = pool.obtain();
				effect.setPosition(screenX, Gdx.graphics.getHeight() - screenY);
				effects.add(effect);
				return true;
			}
		});
		batch = new SpriteBatch();

		prototype = new ParticleEffect();
		prototype.load(Gdx.files.internal("particles.party"), Gdx.files.internal(""));
		prototype.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		prototype.start();

		pool = new ParticleEffectPool(prototype, 0, MAX_POOL_SIZE);
		effects = new Array<ParticleEffectPool.PooledEffect>();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		for (ParticleEffectPool.PooledEffect effect : effects) {
			effect.draw(batch, Gdx.graphics.getDeltaTime());
			if (effect.isComplete()) {
				effects.removeValue(effect, true);
				effect.free();
			}
		}
		Gdx.app.log("pool status", "active: "+effects.size+" | free: "+pool.getFree()+"/"+pool.max+" | record: "+pool.peak);
//		prototype.draw(batch, Gdx.graphics.getDeltaTime());
		batch.end();

	}
	private class InputListener implements InputProcessor {
		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			return false;
		}
	}
}