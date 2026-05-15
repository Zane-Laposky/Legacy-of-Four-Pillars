package model;

public enum Direction{
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public int newX(final Room theRoom){
        int toReturn = 0;
        switch (this){
            case EAST -> toReturn = theRoom.getX() + 1;
            case WEST ->  toReturn = theRoom.getX() - 1;
            default -> toReturn = theRoom.getX();
        }
        return toReturn;
    }
    public int newY(final Room theRoom){
        int toReturn = 0;
        switch (this){
            case NORTH -> toReturn = theRoom.getY() + 1;
            case SOUTH ->  toReturn = theRoom.getY() - 1;
            default -> toReturn = theRoom.getY();
        }
        return toReturn;
    }
}