package com.fruit.logic;

import com.badlogic.gdx.physics.box2d.*;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.misc.Box;
import com.fruit.logic.objects.entities.misc.PlayerProjectile;
import com.fruit.logic.objects.items.Item;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.logic.objects.entities.misc.Projectile;

@SuppressWarnings("all")
public class WorldContactListener implements ContactListener,Constants {

    private final WorldUpdater worldUpdater;

    public WorldContactListener(WorldUpdater worldUpdater){
        this.worldUpdater = worldUpdater;
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        if(f1.getFilterData().categoryBits == PLAYER_BIT || f2.getFilterData().categoryBits == PLAYER_BIT){

            if(f1.getFilterData().categoryBits == ITEM_BIT || f2.getFilterData().categoryBits == ITEM_BIT){
                if(f1.getFilterData().categoryBits == ITEM_BIT){
                    Player player = (Player)f2.getBody().getUserData();
                    Item item = (Item)f1.getBody().getUserData();
                    item.onPickUp(player);
                } else {
                    Player player = (Player)f1.getBody().getUserData();
                    Item item = (Item)f2.getBody().getUserData();
                    item.onPickUp(player);
                }
            }
            if(f1.getFilterData().categoryBits == TREASURE_BIT || f2.getFilterData().categoryBits == TREASURE_BIT){
                if(f1.getFilterData().categoryBits == TREASURE_BIT){
                    //TODO CHANGE IT
                    Box box = (Box)f1.getBody().getUserData();
                    box.killYourself();
                }else {
                    //TODO CHANGE IT
                    Box box = (Box)f2.getBody().getUserData();
                    box.killYourself();
                }
            }

        }

        if(f1.getFilterData().categoryBits == CLUTTER_BIT || f2.getFilterData().categoryBits == CLUTTER_BIT){

        }

        if(f1.getFilterData().categoryBits == ENEMY_BIT || f2.getFilterData().categoryBits == ENEMY_BIT){
            if(f1.getFilterData().categoryBits == PLAYER_BIT || f2.getFilterData().categoryBits == PLAYER_BIT){
                if(f1.getFilterData().categoryBits == PLAYER_BIT){
                    Player p = (Player) f1.getBody().getUserData();
                    Enemy e = (Enemy) f2.getBody().getUserData();
                    e.onDirectContact(p);
                }else {
                    Player p = (Player) f2.getBody().getUserData();
                    Enemy e = (Enemy) f1.getBody().getUserData();
                    e.onDirectContact(p);
                }
            }

        }
        if(f1.getFilterData().categoryBits == PROJECTILE_BIT || f2.getFilterData().categoryBits == PROJECTILE_BIT){
            if(f1.getFilterData().categoryBits == ENEMY_BIT || f2.getFilterData().categoryBits == ENEMY_BIT){
                if(f1.getFilterData().categoryBits== ENEMY_BIT){
                    Enemy enemy = (Enemy)f1.getBody().getUserData();
                    PlayerProjectile projectile = (PlayerProjectile)f2.getBody().getUserData();
                    projectile.onHit(enemy);
                }else {
                    Enemy enemy = (Enemy)f2.getBody().getUserData();
                    PlayerProjectile projectile = (PlayerProjectile)f1.getBody().getUserData();
                    projectile.onHit(enemy);
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
            } else {
                if(f1.getFilterData().categoryBits == PROJECTILE_BIT){
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
