package it.polimi.se2019.model;

import it.polimi.se2019.model.weapon.Weapon;

import java.util.EnumMap;
import java.util.Map;

public class Player {
    private AmmoValue mAmmo;
    private PowerUpCard[] mPowerUpCards = new PowerUpCard[4];
    private Weapon[] mWeapons = new Weapon[3];
    private PlayerColor mColor;
    private int mDeathsNum = 0;
    private PlayerColor[] mDamageTaken = new PlayerColor[12];
    private Map<PlayerColor, Integer> mMarks = new EnumMap<>(PlayerColor.class);
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

    public Map<PlayerColor, Integer> getMarks() {
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

    public PowerUpCard getPowerUpCard(int index) {
        return mPowerUpCards[index];
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
     * return if player has reached the maximum number of weapon in his hand
     * @return true if has three weapons, false otherwise
     */
    public boolean isFullOfWeapons () {
        for (Weapon weapon : mWeapons) {
            if (weapon == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * return if player has reached the maximum number of power up cards in his hand
     * @return true if has three power ups, false otherwise
     */
    public boolean isFullOfPowerUps () {
        for (PowerUpCard powerUpCard : mPowerUpCards) {
            if (powerUpCard == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * takes a weapon (is used in Action class to exchange weapon when the hand of player is full)
     * @param index is the index of the weapon to add in your hand
     * @return
     */
    public Weapon takeWeapon (int index) {
        Weapon weapon = mWeapons[index];
        mWeapons[index] = null;
        return weapon;
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

    /**
     * Update score of a player
     * @param value is the value to add to the current score
     */
    public void addScore(int value) {
        mScore += value;
    }

    /**
     * Update the damage taken of a player
     * @param attackingPlayer is player that attacks
     * @param damage value of damage to add to the current damage
     */
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

    /**
     * Update the marks on a player
     * @param attackingPlayer is player that attacks
     * @param marks value of marks to add to the current marks
     */
    public void sufferedMarks(PlayerColor attackingPlayer,int marks) {
        if(marks + getMarks().get(attackingPlayer) >= MAX_MARKS) {
            mMarks.put(attackingPlayer, MAX_MARKS);
        }
        else{
            mMarks.put(attackingPlayer,marks);
        }
    }

    /**
     * Increase the numbers of deaths of player
     */
    public void incrementDeaths() {
        mDeathsNum += 1;
    }

    /**
     * Add a weapon to player hand and throws exception in case player hand is full
     * @param value is the weapon to add
     * @throws FullHandException
     */
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

    /**
     * set player in death status
     */
    public void setDeadStatus(){
        if(mDamageTaken[10] != null) {
            mDead = true;
        }
        else {
            mDead = false;
        }
    }

    /**
     * add powerup card in player hand and throw exception when player reaches the maximum number of powerups card
     * @param value powerup card to add
     * @param isRespawn boolean value to know if a player could have four powerups instead of three
     * @throws FullHandException
     */
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

    /**
     * discard a card from the hand of player
     * @param card card to discard
     */
    public void discard(PowerUpCard card) {
        for (int i = 0; i < mPowerUpCards.length; i++) {
            if (mPowerUpCards[i] == card) {
                mPowerUpCards[i] = null;
                return ;
            }
        }
    }

    public void discard (int powerUpIndex) {
        mPowerUpCards[powerUpIndex] = null;
    }

    /**
     * set player on a position
     * @param value the position where to put player
     */
    public void move(Position value) {
        mPos = value;
    }

    /**
     * respawn the player
     * @param value the position where to put player
     */
    public void respawn(Position value) {
        for (int i = 0; i < mDamageTaken.length; i++) {
            mDamageTaken[i] = null;
        }
        incrementDeaths();
        setDeadStatus();
        move(value);
    }

    /**
     * combine sufferedDamage,sufferedMarks,setDeadStatus methods
     * @param damage damage to add to the current damage of the player
     * @param shooterColor the attacking player
     */
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
