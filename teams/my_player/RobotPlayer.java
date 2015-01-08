package player1;

import java.util.ArrayList;
import java.util.Random; 

import battlecode.common.*;

public class RobotPlayer {
	static Direction facing;
	static Random rand = new Random();
	public static RobotController rc;
	public static Direction all_directions[] = Direction.values();

	static ArrayList<MapLocation> path = new ArrayList<MapLocation>();


	public static void run(RobotController myrc){
		rc = myrc;
		rand.setSeed(rc.getID()); // each robot will follow its own random path
		facing = getRandomDirection();




		while (true){
			try {
				// HQ can spawn units 
				if (rc.getType() == RobotType.HQ){
					attackEnemyZero();
					spawnUnit(RobotType.BEAVER);
				} else if (rc.getType() == RobotType.BEAVER) { 
					// at first, building miners is more important 
					if (Clock.getRoundNum() < 700){ 
						buildStructure(RobotType.MINERFACTORY);
					} else { // then we build barracks 
						buildStructure(RobotType.BARRACKS);
					}
					mineAndMove();

				} else if (rc.getType() == RobotType.MINER){
					mineAndMove();

				} else if (rc.getType() == RobotType.MINERFACTORY){
					spawnUnit(RobotType.MINER);

				} else if (rc.getType() == RobotType.BARRACKS){
					spawnUnit(RobotType.SOLDIER);

				} else if (rc.getType() == RobotType.TOWER){
					attackEnemyZero();

				} else if (rc.getType() == RobotType.SOLDIER){
					attackEnemyZero();
					moveAround();
				}

			} catch (GameActionException e) {
				e.printStackTrace();
			}
			rc.yield();
		}
	}

	private static void attackEnemyZero() throws GameActionException {
		RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(rc.getLocation(), rc.getType().attackRadiusSquared, rc.getTeam().opponent());
		
		if (nearbyEnemies.length > 0){
			if (rc.isWeaponReady() && rc.canAttackLocation(nearbyEnemies[0].location)){
				rc.attackLocation(nearbyEnemies[0].location);
			}
		}	
	}

	private static void spawnUnit(RobotType type) throws GameActionException {
		if (rc.isCoreReady() && rc.canSpawn(Direction.NORTH, type)){
			rc.spawn(Direction.NORTH, type);
		}
	}

	private static void moveAround() throws GameActionException {
		
		if (rand.nextDouble() < 0.05){
			if (rand.nextDouble() < 0.05) {
				facing = facing.rotateLeft();
			} else {
				facing = facing.rotateRight();
			} 
		}
		MapLocation tileInFront = rc.getLocation().add(facing);
		

		// check that location in front cannot be attacked by the enemy 
		MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
		boolean tileInFrontSafe = true;
		for (MapLocation m : enemyTowers){
			if (m.distanceSquaredTo(tileInFront) <=  RobotType.TOWER.attackRadiusSquared){
				tileInFrontSafe = false;
				break; 
			}
		}

		if (rc.senseTerrainTile(tileInFront) != TerrainTile.NORMAL || !tileInFrontSafe){
			facing = facing.rotateLeft();
		} else {
			if (rc.isCoreReady() && rc.canMove(facing)){
				rc.move(facing);
			}
		}
	} 

	private static void mineAndMove() throws GameActionException {
		// check if there is ore in current location 	
		if (rc.senseOre(rc.getLocation()) > 1){
			if (rc.isCoreReady() && rc.canMine()) {
				rc.mine(); // if so, then mine it 
			}
		} else { 
			moveAround();
		}
	}

	private static Direction getRandomDirection(){
		// randomize direction 
		return Direction.values()[(int)(rand.nextDouble()*8)];
	}

	private static void buildStructure(RobotType type) throws GameActionException {
		if (rc.getTeamOre() > type.oreCost){
			Direction build_dir = getRandomDirection();
			if (rc.isCoreReady() && rc.canBuild(build_dir, type) ){
				rc.build(build_dir, type);
			}
		}
	}

}