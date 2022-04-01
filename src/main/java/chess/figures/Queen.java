package chess.figures;

import javax.swing.ImageIcon;

import chess.Board;
import chess.BoardMovement;
import chess.services.GlobalContext;
import chess.util.Move;
import chess.util.Patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static chess.util.Patterns.diagonalPattern;
import static chess.util.Patterns.horizontalAndVerticalPattern;

public class Queen extends Figure {

	private static final ImageIcon iconWhite = new ImageIcon(
			"images\\white_set\\Queen.png");
	private static final ImageIcon iconBlack = new ImageIcon(
			"images\\black_set\\Queen.png");

	public Queen(String Col, int x, int y, Board b) {
		if (Col.equalsIgnoreCase("white"))
			this.icon = iconWhite;
		else
			this.icon = iconBlack;

		super.color = Col;
		super.position[0] = x;
		super.position[1] = y;
		super.board = b;
	}

	@Override
	public void markAttacks() {
		Patterns.applyDiagonalPatternFromPosition(this.getX(), this.getY(), this.getColor());
		Patterns.applyHorizontalAndVerticalPatternFromPosition(this.getX(), this.getY(), this.getColor());
	}

	@Override
	public List<Move> getValidMoves() {
		BoardMovement boardMovement = GlobalContext.getBoardMovement();

		List<int[]> patterns = new ArrayList<>();
		Collections.addAll(patterns, horizontalAndVerticalPattern);
		Collections.addAll(patterns, diagonalPattern);

		return patterns.stream().filter((pattern) -> {
			int xFrom = getX();
			int yFrom = getY();
			int xTo = (xFrom + pattern[0]) % 8;
			int yTo = (yFrom + pattern[1]) % 8;
			return boardMovement.isMoveValid(xFrom, yFrom, xTo, yTo, false);
		}).map(pattern -> new Move(getX(), getY(), (getX() + pattern[0]) % 8, (getY() + pattern[1]) % 8)).collect(Collectors.toList());

	}

	@Override
	public boolean isTargetLocationValid(int x, int y) {
		boolean checkX = x == this.getX();
		boolean checkY = y == this.getY();
		boolean checkXY = Math.abs(x - this.getX()) == Math.abs(y
				- this.getY());

		return checkX || checkY || checkXY;
	}

}
