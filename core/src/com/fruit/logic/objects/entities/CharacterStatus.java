package com.fruit.logic.objects.entities;

/**
 * @Author FruitAddict
 */
public class CharacterStatus {
    //stats booleans
    private boolean burning,
            freezing,
            healing,
            overpowering,
            enraged,
            dying,
            poisoned,
            shielded= false;

    public boolean isBurning() {
        return burning;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    public boolean isFreezing() {
        return freezing;
    }

    public void setFreezing(boolean freezing) {
        this.freezing = freezing;
    }

    public boolean isHealing() {
        return healing;
    }

    public void setHealing(boolean healing) {
        this.healing = healing;
    }

    public boolean isOverpowering() {
        return overpowering;
    }

    public void setOverpowering(boolean overpowering) {
        this.overpowering = overpowering;
    }

    public boolean isEnraged() {
        return enraged;
    }

    public void setEnraged(boolean enraged) {
        this.enraged = enraged;
    }

    public boolean isDying() {
        return dying;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }


    public boolean isPoisoned() {
        return poisoned;
    }

    public void setPoisoned(boolean poisoned) {
        this.poisoned = poisoned;
    }

    public boolean isShielded() {
        return shielded;
    }

    public void setShielded(boolean shielded) {
        this.shielded = shielded;
    }
}
