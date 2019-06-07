package it.polimi.se2019.model.weapon;

public class WeaponsTest {
    /******************************************************************************************************/
    /* private ShootContext mAllInOriginContext;                                                          */
    /*                                                                                                    */
    /* // utility function for checking optionals in tests                                                */
    /* public static <T> T failOptionalCheck(Class<T> toReturn) {                                         */
    /*     assertTrue(false);                                                                             */
    /*     return null;                                                                                   */
    /* }                                                                                                  */
    /*                                                                                                    */
    /* @Before                                                                                            */
    /* public void instantiate() {                                                                        */
    /*     // mario bros                                                                                  */
    /*     mAllInOriginContext = new ShootContext(                                                        */
    /*             Board.fromJson(Jsons.get("boards/game/board1")),                                       */
    /*             new HashSet(Arrays.asList(                                                             */
    /*                     new Player("Mario", PlayerColor.PURPLE, new Position(0, 0)),                   */
    /*                     new Player("Luigi", PlayerColor.GREEN, new Position(0, 0)),                    */
    /*                     new Player("Dorian", PlayerColor.GREY, new Position(0, 0)),                    */
    /*                     new Player("Smurfette", PlayerColor.BLUE, new Position(0, 0)),                 */
    /*                     new Player("Banano", PlayerColor.YELLOW, new Position(0, 0))                   */
    /*             )),                                                                                    */
    /*             PlayerColor.PURPLE                                                                     */
    /*     );                                                                                             */
    /* }                                                                                                  */
    /*                                                                                                    */
    /* @Test                                                                                              */
    /* public void testHeatseekerMarioShootsLuigi() {                                                     */
    /*     // instantiate weapon                                                                          */
    /*     Weapon heatseeker = Weapons.get("heatseeker");                                                 */
    /*                                                                                                    */
    /*     // provide needed information to shoot                                                         */
    /*     mAllInOriginContext.provideInfo(new TargetsLiteral(Collections.singleton(PlayerColor.GREEN))); */
    /*                                                                                                    */
    /*     // produce result with complete context                                                        */
    /*     ShootResult result = heatseeker.shoot(mAllInOriginContext);                                    */
    /*                                                                                                    */
    /*     // assert that result is an action (since context was complete)                                */
    /*     assertTrue(result.isComplete());                                                               */
    /*                                                                                                    */
    /*     // test that action is correct                                                                 */
    /*     Action actual = result.asAction();                                                             */
    /*     Action expected = new WeaponAction(                                                            */
    /*             new DamageAction(                                                                      */
    /*                     PlayerColor.PURPLE,                                                            */
    /*                     Collections.singleton(PlayerColor.GREEN),                                      */
    /*                     new Damage(3, 0)                                                               */
    /*             )                                                                                      */
    /*     );                                                                                             */
    /*     assertEquals(expected, actual);                                                                */
    /* }                                                                                                  */
    /*                                                                                                    */
    /* @Test                                                                                              */
    /* public void testHeatseekerMarioShootsLuigiMissingTarget() {                                        */
    /*      // instantiate weapon                                                                         */
    /*     Weapon heatseeker = Weapons.get("heatseeker");                                                 */
    /*                                                                                                    */
    /*     // shoot                                                                                       */
    /*     Response actual = heatseeker.shoot(mAllInOriginContext).asResponse();                          */
    /*                                                                                                    */
    /*     // AtomicExpression expected = new InflictDamage(                                              */
    /*             // new DamageLiteral(new Damage(3, 0)),                                                */
    /*             // new WaitForInfo()                                                                   */
    /*     // );                                                                                          */
    /*     Response expected = new TargetSelectionResponse(                                               */
    /*             1,                                                                                     */
    /*             1,                                                                                     */
    /*             Collections.emptySet()                                                                 */
    /*     );                                                                                             */
    /*     assertEquals(expected, actual);                                                                */
    /* }                                                                                                  */
    /*                                                                                                    */
    /* @Test                                                                                              */
    /* public void testLockRifleMarioShootsLuigiAndThenDorian() {                                         */
    /*     // instantiate weapon                                                                          */
    /*     Weapon lockrifle = Weapons.get("lock_rifle");                                                  */
    /*                                                                                                    */
    /*     // provide needed information to shoot                                                         */
    /*     mAllInOriginContext.provideInfo(Arrays.asList(                                                 */
    /*             new TargetsLiteral(Collections.singleton(PlayerColor.GREEN)),                          */
    /*             new TargetsLiteral(Collections.singleton(PlayerColor.GREY))                            */
    /*     ));                                                                                            */
    /*                                                                                                    */
    /*     // produce result with complete context                                                        */
    /*     ShootResult result = lockrifle.shoot(mAllInOriginContext);                                     */
    /*                                                                                                    */
    /*     // assert that result is an action (since context was complete)                                */
    /*     assertTrue(result.isComplete());                                                               */
    /*                                                                                                    */
    /*     // test that action is correct                                                                 */
    /*     Action actual = result.asAction();                                                             */
    /*     Action expected = new WeaponAction(                                                            */
    /*             new DamageAction(                                                                      */
    /*                     PlayerColor.PURPLE,                                                            */
    /*                     Collections.singleton(PlayerColor.GREEN),                                      */
    /*                     new Damage(2, 1)                                                               */
    /*             ),                                                                                     */
    /*             new DamageAction(                                                                      */
    /*                     PlayerColor.PURPLE,                                                            */
    /*                     Collections.singleton(PlayerColor.GREY),                                       */
    /*                     new Damage(0, 1)                                                               */
    /*             )                                                                                      */
    /*     );                                                                                             */
    /*     assertEquals(expected, actual);                                                                */
    /* }                                                                                                  */
    /******************************************************************************************************/
}
