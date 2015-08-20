package multiplicationtables;

import java.util.ArrayList;

public class MultiplicationProblem {
	private static int[] multipliers;
	private static ArrayList<Boolean> answersCorrect;
	private static boolean order = false;

	public MultiplicationProblem(int multiplier1, int multiplier2) {
		multipliers = new int[2];
		multipliers[0] = multiplier1;
		multipliers[1] = multiplier2;
		answersCorrect = new ArrayList<Boolean>();
	}

	public void setAnswerCorrect(boolean answerCorrect) {
		answersCorrect.add(answerCorrect);
	}

	public float answerAverageTruth() {
		float total = 0;
		for (int i = 0; i < answersCorrect.size(); i++) {
			total = total + (answersCorrect.get(i) ? 1 : 0);
		}
		return total / answersCorrect.size();
	}

	@Override
	public String toString() {
		return multipliers[0] + " x " + multipliers[1] + " avg: "
				+ answerAverageTruth();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof MultiplicationProblem) {
			MultiplicationProblem other = (MultiplicationProblem) object;

			int[] otherMultipliers = other.getOrderedMultipliers();
			int[] thisMultipliers = getOrderedMultipliers();

			if ((otherMultipliers[0] == thisMultipliers[0] && otherMultipliers[1] == thisMultipliers[1])
					|| (otherMultipliers[0] == thisMultipliers[1] && otherMultipliers[1] == thisMultipliers[0])) {
				return true;
			}
		}
		return false;
	}

	public int[] getDisplayMultipliers() {
		int[] displayMultipliers = order ? new int[] { multipliers[0],
				multipliers[1] } : new int[] { multipliers[1], multipliers[0] };
		return displayMultipliers;
	}

	public int[] getOrderedMultipliers() {
		return multipliers;
	}

	public boolean getOrder() {
		return order;
	}

	public void pickOrder() {
		order = Math.random() > 50;
	}
}
