package com.bitboards;

import com.bitboards.BitBoard.Piece;
import lombok.Getter;

@Getter
public class Move {
    private final int currentLocation;
    private final int destinationLocation;
    private final Piece movedPiece;

    public Move(final int currentLocation,
                final int destinationLocation,
                final Piece moved) {
        this.currentLocation = currentLocation;
        this.destinationLocation = destinationLocation;
        this.movedPiece = moved;
    }

    @Override
    public String toString() {
        return BitBoard.getPositionAtCoordinate(this.currentLocation) + "-"
                + BitBoard.getPositionAtCoordinate(this.destinationLocation);
    }

    @Override
    public int hashCode() {
        return this.movedPiece.hashCode() +
                this.currentLocation +
                this.destinationLocation;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move otherMove)) {
            return false;
        }
        return (this.movedPiece == otherMove.getMovedPiece())
                && (this.currentLocation == otherMove.getCurrentLocation())
                && (this.destinationLocation == otherMove.getDestinationLocation());
    }

}