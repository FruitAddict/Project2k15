package com.fruit.logic;

import com.badlogic.gdx.physics.box2d.*;
import com.fruit.Controller;
import com.fruit.logic.objects.Projectile;
import com.fruit.logic.objects.abstracted.Character;

import java.util.Random;

@SuppressWarnings("all")
public class WorldContactListener implements ContactListener,Constants {

    private final WorldUpdater worldUpdater;

    //this is only for testing purposes
    Random random = new Random();

    public WorldContactListener(WorldUpdater worldUpdater){
        this.worldUpdater = worldUpdater;
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        if(f1.getFilterData().categoryBits == PLAYER_BIT || f2.getFilterData().categoryBits == PLAYER_BIT){

        }

        if(f1.getFilterData().categoryBits == CLUTTER_BIT || f2.getFilterData().categoryBits == CLUTTER_BIT){

        }

        if(f1.getFilterData().categoryBits == ENEMY_BIT || f2.getFilterData().categoryBits == ENEMY_BIT){

        }
        if(f1.getFilterData().categoryBits == PROJECTILE_BIT || f2.getFilterData().categoryBits == PROJECTILE_BIT){
            if(f1.getFilterData().categoryBits == ENEMY_BIT || f2.getFilterData().categoryBits == ENEMY_BIT){
                if(f1.getFilterData().categoryBits== ENEMY_BIT){

                    //todo actual attacking alghorithm
                    Character character = (Character)f1.getBody().getUserData();
                    Projectile projectile = (Projectile)f2.getBody().getUserData();
                    projectile.killYourself();
                    int damage = 1+random.nextInt(5);
                    character.changeHealthPoints(-damage);
                }else {
                    Character character = (Character)f2.getBody().getUserData();
                    Projectile projectile = (Projectile)f1.getBody().getUserData();
                    projectile.killYourself();
                    int damage = 1+random.nextInt(5);
                    character.changeHealthPoints(-damage);
                }
            }
            else if(f1.getFilterData().categoryBits == TERRAIN_BIT || f2.getFilterData().categoryBits == TERRAIN_BIT){
                if(f1.getFilterData().categoryBits == PROJECTILE_BIT) {
                    Projectile projectile = (Projectile)f1.getBody().getUserData();
                    projectile.killYourself();
                } else{
                    Projectile projectile = (Projectile)f2.getBody().getUserData();
                    projectile.killYourself();
                }
            }
            else if(f1.getFilterData().categoryBits == CLUTTER_BIT || f2.getFilterData().categoryBits == CLUTTER_BIT){
                if(f1.getFilterData().categoryBits== PROJECTILE_BIT){
                    Projectile projectile = (Projectile)f1.getBody().getUserData();
                    projectile.killYourself();
                }else {
                    Projectile projectile = (Projectile)f2.getBody().getUserData();
                    projectile.killYourself();
                }
            }

        }
        if(f1.getFilterData().categoryBits == PORTAL_BIT || f2.getFilterData().categoryBits == PORTAL_BIT){
            if(f1.getFilterData().categoryBits == PLAYER_BIT || f2.getFilterData().categoryBits == PLAYER_BIT){
                if(f1.getFilterData().categoryBits == PLAYER_BIT){
                    switch((Integer)f2.getBody().getUserData()){
                        case NORTH_DIR: {
                            worldUpdater.getMapManager().requestChange(NORTH_DIR);
                            break;
                        }
                        case SOUTH_DIR: {
                            worldUpdater.getMapManager().requestChange(SOUTH_DIR);
                            break;
                        }
                        case WEST_DIR: {
                            worldUpdater.getMapManager().requestChange(WEST_DIR);
                            break;
                        }
                        case EAST_DIR: {
                            worldUpdater.getMapManager().requestChange(EAST_DIR);
                            break;
                        }
                    }
                }else {
                    switch((Integer)f1.getBody().getUserData()){
                        case NORTH_DIR: {
                            worldUpdater.getMapManager().requestChange(NORTH_DIR);
                            break;
                        }
                        case SOUTH_DIR: {
                            worldUpdater.getMapManager().requestChange(SOUTH_DIR);
                            break;
                        }
                        case WEST_DIR: {
                            worldUpdater.getMapManager().requestChange(WEST_DIR);
                            break;
                        }
                        case EAST_DIR: {
                            worldUpdater.getMapManager().requestChange(EAST_DIR);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
