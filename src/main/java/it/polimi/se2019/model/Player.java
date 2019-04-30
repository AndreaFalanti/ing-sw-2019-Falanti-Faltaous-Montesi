package it.polimi.se2019.model;

import java.util.EnumMap;

public class Player {
    private AmmoValue mAmmo;
    private PowerUpCard[] mPowerUpCards = new PowerUpCard[4];
    private Weapon[] mWeapons = new Weapon[3];
    private PlayerColor mColor;
    private int mDeathsNum = 0;
    private PlayerColor[] mDamageTaken = new PlayerColor[12];
    private EnumMap<PlayerColor, Integer> mMarks = new EnumMap<>(PlayerColor.class);
    private int mScore=0;
    private Position mPos;
    private String mName;
    private boolean mDead = false;
    private boolean mBoardFlipped = false;
    private static final int MAX_MARKS = 3;

    private void initializeMarksMap () {
        mMarks.put(PlayerColor.YELLOW,0);
        mMarks.put(PlayerColor.GREEN,0);
        mMarks.put(PlayerColor.PURPLE,0);
        mMarks.put(PlayerColor.BLUE,0);
        mMarks.put(PlayerColor.GREY,0);
    }

    public Player (String name, PlayerColor color) {
            mName = name;
            mColor = color;
            mAmmo = new AmmoValue(1, 1, 1);
            initializeMarksMap();
    }

    //region GETTERS
    public AmmoValue getAmmo() {
        return mAmmo;
    }

    public PlayerColor[] getDamageTaken() {
        return mDamageTaken;
    }

    public EnumMap<PlayerColor, Integer> getMarks() {
        return mMarks;
    }

    public PlayerColor getColor() {
        return mColor;
    }

    public int getDeathsNum() {
        return mDeathsNum;
    }

    public Weapon[] getWeapons() {
        return mWeapons;
    }

    public boolean isDead() {
        return mDead;
    }

    public PowerUpCard[] getPowerUps() {
        return mPowerUpCards;
    }

    public int getScore() {
        return mScore;
    }

    public Position getPos() {
        return mPos;
    }

    public String getName() {
        return  mName;
    }

    public boolean isBoardFlipped() {
        return mBoardFlipped;
    }

    public Weapon getWeapon(int index) {
        return mWeapons[index];
    }
    //endregion

    public void flipBoard () {
        mBoardFlipped = true;
    }

    public boolean isOverkilled () {
        return mDamageTaken[11] != null;
    }

    public boolean hasNoDamage () {
        return mDamageTaken[0] == null;
    }

    /**
     * Get max grab distance of player. If it has at least 3 damage, unlock "adrenaline grab".
     * @return 1 if damage < 3, 2 if >= 3
     */
    public int getMaxGrabDistance () {
        return (mDamageTaken[2] != null) ? 2 : 1;
    }

    /**
     * Return if player can perform "adrenaline move and shoot".
     * @return true if has at least 6 damage, false otherwise
     */
    public boolean canMoveBeforeShooting () {
        return mDamageTaken[5] != null;
    }

    public void addScore(int value) {
        mScore += value;
    }

    public void sufferedDamage(PlayerColor attackingPlayer,int damage) {
        int i = 0;
        int j = 0;

        damage += getMarks().get(attackingPlayer);
        while(i <= mDamageTaken.length - 1 && mDamageTaken[i] != null)
            i++;
        if(i <= mDamageTaken.length - 1) {
            while(j <= damage - 1 && i <= mDamageTaken.length - 1) {
                mDamageTaken[i] = attackingPlayer;
                j++;
                i++;
            }
        }
        mMarks.put(attackingPlayer,0);
    }

    public void sufferedMarks(PlayerColor attackingPlayer,int marks) {
        if(marks + getMarks().get(attackingPlayer) >= MAX_MARKS) {
            mMarks.put(attackingPlayer, MAX_MARKS);
        }
        else{
            mMarks.put(attackingPlayer,marks);
        }
    }

    public void incrementDeaths() {
        mDeathsNum += 1;
    }

    public void addWeapon(Weapon value) throws FullHandException {
        int i=0;

        while(i <= mWeapons.length - 1 && mWeapons[i] != null )
            i++;
        if(i <= mWeapons.length -1) {
            mWeapons[i] = value;
        }
        else {
            throw new FullHandException("You have reached the maximum number of weapons in your hand");
        }
    }

    public void setDeadStatus(){
        if(mDamageTaken[10] != null) {
            mDead = true;
        }
        else {
            mDead = false;
        }
    }

    public void addPowerUp(PowerUpCard value, boolean isRespawn) throws FullHandException {
        int lengthToCheck = isRespawn ? mPowerUpCards.length : (mPowerUpCards.length - 1);
        for (int i = 0; i < lengthToCheck; i++) {
            if (mPowerUpCards[i] == null) {
                mPowerUpCards[i] = value;
                return;
            }
        }
        throw new FullHandException ("PowerUp hand is full, can't draw another card");
    }

    public void addPowerUp (PowerUpCard value) throws FullHandException{
        addPowerUp(value, false);
    }

    public void discard(PowerUpCard card) {
        for (int i = 0; i < mPowerUpCards.length; i++) {
            if (mPowerUpCards[i] == card) {
                mPowerUpCards[i] = null;
                return ;
            }
        }
    }

    public void move(Position value) {
        mPos = value;
    }

    public void respawn(Position value) {
        for (int i = 0; i < mDamageTaken.length; i++) {
            mDamageTaken[i] = null;
        }
        incrementDeaths();
        setDeadStatus();
        move(value);
    }

    public void onDamageTaken (Damage damage, PlayerColor shooterColor) {
        sufferedDamage(shooterColor, damage.getDamage());
        sufferedMarks(shooterColor, damage.getMarksNum());
        setDeadStatus();
    }

    public void reloadWeapon (int weaponIndex) {
        Weapon weapon = getWeapon(weaponIndex);

        //TODO: useless exception in AmmoValue subtract? (verified in controller)
        try {
            getAmmo().subtract(weapon.getReloadCost());
            weapon.setLoaded(true);
        } catch (NotEnoughAmmoException e) {
            e.printStackTrace();
        }
    }
}
