package player1;
import battlecode.common.*;
import java.util.*;
import java.util.Random; // this should give the same import 

public class RobotPlayer {
	static Direction facing;
	static Random rand; 
	static RobotController rc;

	public static void run(RobotController myrc){
		rc = myrc;
		rand = new Random(myrc.getID()); // each robot will follow its own random path
		facing = getRandomDirection();
		while (true){
			try {
				// HQ can spawn units 
				if (rc.getType() == RobotType.HQ){
					spawnUnit(RobotType.BEAVER);
				} else if (rc.getType() == RobotType.BEAVER) { 
					buildStructure(RobotType.MINERFACTORY);
					mineAndMove();
				} else if (rc.getType() == RobotType.MINER){
					mineAndMove();
				} else if (rc.getType() == RobotType.MINERFACTORY){
					spawnUnit(RobotType.MINER);
				}
			} catch (GameActionException e) {
				e.printStackTrace();
			}
			rc.yield();
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
		
		if (rc.senseTerrainTile(rc.getLocation().add(facing)) != TerrainTile.NORMAL){
			facing = facing.rotateLeft();
		}
		if (rc.isCoreReady() && rc.canMove(facing)){
			rc.move(facing);
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