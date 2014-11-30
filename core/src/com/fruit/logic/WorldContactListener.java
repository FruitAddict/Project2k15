package com.fruit.logic;

import com.badlogic.gdx.physics.box2d.*;
import com.fruit.logic.objects.Projectile;
import com.fruit.tests.MindlessWalker;
@SuppressWarnings("all")
public class WorldContactListener implements ContactListener,Constants {
    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        System.out.println(f1.getFilterData().categoryBits);
        System.out.println(f2.getFilterData().categoryBits);

        if(f1.getFilterData().categoryBits == PLAYER_BIT || f2.getFilterData().categoryBits == PLAYER_BIT){

        }
        if(f1.getFilterData().categoryBits == CLUTTER_BIT || f2.getFilterData().categoryBits == CLUTTER_BIT){

        }
        if(f1.getFilterData().categoryBits == ENEMY_BIT || f2.getFilterData().categoryBits == ENEMY_BIT){

        }
        if(f1.getFilterData().categoryBits == PROJECTILE_BIT || f2.getFilterData().categoryBits == PROJECTILE_BIT){
            if(f1.getFilterData().categoryBits == ENEMY_BIT || f2.getFilterData().categoryBits == ENEMY_BIT){
                if(f1.getBody().getUserData() instanceof MindlessWalker){
                    MindlessWalker walker = (MindlessWalker)f1.getBody().getUserData();
                    Projectile projectile = (Projectile)f2.getBody().getUserData();
                    projectile.killYourself();
                    walker.killYourself();
                }else if( f2.getBody().getUserData() instanceof MindlessWalker){
                    MindlessWalker walker = (MindlessWalker)f2.getBody().getUserData();
                    Projectile projectile = (Projectile)f1.getBody().getUserData();
                    projectile.killYourself();
                    walker.killYourself();
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

                }else {

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
