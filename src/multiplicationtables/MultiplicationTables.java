package multiplicationtables;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;

public class MultiplicationTables extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String stringInput = "";

	float timer = 0;
	float timerMultiplier = 0.1f;
	float answerTime = 1.75f;
	int timerSize = 1000;

	MultiplicationProblem currentProblem;

	int inputMaxLength = 3;

	int score = 0;
	int tries = 0;

	int color = 0;

	int questionFontSize = 150;

	float[] animateOutValues = new float[5];

	float animateTransitionTimer = 0;

	boolean animatingOut = false;
	boolean animatingIn = false;

	boolean answerSubmitted = false;
	boolean answerCorrect = false;

	String multiplicationSymbol = "*";

	PFont font, font2, font3;

	ArrayList<MultiplicationProblem> problems;

	String outputFileName = "problems.mts";

	public void setup() {
		size(1280, 720);

		generateProblems();
		loadInfoFromFile();
		writeInfoToFile();
		pickProblem();

		font = loadFont("RobotoCondensed-LightItalic-120.vlw");
		font2 = loadFont("RobotoCondensed-Light-120.vlw");
		font3 = loadFont("RobotoCondensed-LightItalic-30.vlw");
	}

	private void generateProblems() {
		problems = new ArrayList<MultiplicationProblem>();
		for (int m1 = 1; m1 <= 12; m1++) {
			for (int m2 = 1; m2 <= 12; m2++) {
				MultiplicationProblem problem = new MultiplicationProblem(m1,
						m2);
				boolean isUnique = true;
				if (problems.size() > 0) {
					for (int p = 0; p < problems.size(); p++) {
						if (problem.equals(problems.get(p))) {
							isUnique = false;
						}
					}
				}
				if (isUnique) {
					problems.add(problem);
					System.out.print(problem + "\n");
				}
			}
		}
		// System.out.println(problems.get(5).toString() + " " + count);
	}

	public void draw() {
		background(100, 100, 100);

		textAlign(CENTER, CENTER);

		noStroke();

		if (animatingOut) {
			// textFont(font, 110);
			// fill(255, 255);
			// text((int) animateOutValues[1] + " * " + (int)
			// animateOutValues[2], 200, 100);

			if (answerCorrect) {
				fill(lerpColor(color(255, 200), color(0, 166, 81),
						Math.min((animateTransitionTimer / 200), 1)));
			} else {
				textFont(font, 110);
				fill(255, 255);
				text(currentProblem.getDisplayMultipliers()[0] + " "
						+ multiplicationSymbol + " "
						+ currentProblem.getDisplayMultipliers()[1], width / 2,
						height / 3);
				fill(255, 200);
			}

			textFont(font2,
					lerp(90, 120, Math.min((animateTransitionTimer / 200), 1)));
			text((int) (animateOutValues[1] * animateOutValues[2]),
					width / 2,
					lerp(height / 1.75f, height / 2f,
							Math.min((animateTransitionTimer / 200), 1)));

			animateTransitionTimer += 6;

			if (animateTransitionTimer >= 500) {
				animateTransitionTimer = 0;
				animatingOut = false;
				animatingIn = true;
				pickProblem();
				animateOutValues = new float[5];
			}
		} else if (animatingIn) {
			fill(color = lerpColor(color(242, 101, 34), color(0, 166, 81),
					(250 - timer) / 100));
			ellipse(width / 2, height / 2, timer, timer);
			timer = timer + (answerTime + (timerMultiplier));
			timerMultiplier = timerMultiplier + (timerMultiplier / 750);

			// rect(0, 200-(timer/4), 400, (timer/2));

			fill(0, 0, 0,
					lerp(0, 25, Math.min(animateTransitionTimer / 100, 1)));
			ellipse(width / 2, height / 2, timerSize, timerSize);

			textFont(font, questionFontSize);
			fill(255, lerp(0, 255, Math.min(animateTransitionTimer / 100, 1)));
			text(currentProblem.getDisplayMultipliers()[0] + " "
					+ multiplicationSymbol + " "
					+ currentProblem.getDisplayMultipliers()[1], width / 2,
					height / 2.25f);

			textFont(font2, 90);
			fill(255, lerp(0, 200, Math.min(animateTransitionTimer / 100, 1)));
			text(stringInput, width / 2, height / 2.25f);

			animateTransitionTimer += 6;

			if (animateTransitionTimer >= 200) {
				animatingIn = false;
				answerCorrect = false;
				animateTransitionTimer = 0;
			}
		} else {
			fill(color = lerpColor(color(242, 101, 34), color(0, 166, 81),
					(250 - timer) / 100));
			ellipse(width / 2, height / 2, timer, timer);
			timer = timer + (answerTime + (timerMultiplier));
			timerMultiplier = timerMultiplier + (timerMultiplier / 750);

			// rect(0, 200-(timer/4), 400, (timer/2));

			fill(0, 0, 0, 25);
			ellipse(width / 2, height / 2, timerSize, timerSize);

			textFont(font, questionFontSize);
			fill(255, 255);
			text(currentProblem.getDisplayMultipliers()[0] + " "
					+ multiplicationSymbol + " "
					+ currentProblem.getDisplayMultipliers()[1], width / 2,
					height / 2.25f);

			textFont(font2, 90);
			fill(255, 200);
			text(stringInput, width / 2, height / 1.60f);

			arc(360, 360, 50, 50, 0, (PI / 10) * score, PIE);

			if (stringInput.length() > 0) {
				if (Integer.valueOf(stringInput) == currentProblem
						.getDisplayMultipliers()[0]
						* currentProblem.getDisplayMultipliers()[1]) {
					score++;
					setAnimateOutValues(true);
					resetGUI();
					answerCorrect = true;
					tries++;
				}
			}

			if (timer >= timerSize || answerSubmitted) {
				answerSubmitted = false;
				// Here the score doesn't change, but the tries number goes up
				setAnimateOutValues(true);
				resetGUI();
				answerCorrect = false;
				tries++;
			}
		}
		fill(255, 255);
		textFont(font3, 30);
		text(score + "/" + tries, width - 50, height - 50);
	}

	public void keyPressed() {
		if (!animatingOut && !animatingIn) {
			if (key >= '0' && key <= '9') {
				if (stringInput.length() < inputMaxLength) {
					stringInput += key;
				}
			}
			if (key == BACKSPACE) {
				if (stringInput.length() > 0) {
					stringInput = stringInput.substring(0,
							stringInput.length() - 1);
				}
			}
			if (key == ENTER && stringInput.length() > 0) {
				answerSubmitted = true;
			}
		}
	}

	public void pickProblem() {
		float[] weights = { 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f,
				0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 0.3f, 0.3f, 0.3f,
				0.3f, 0.3f, 0.3f, 0.3f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f,
				0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.6f, 0.6f, 0.6f, 0.6f, 0.7f,
				0.7f, 0.7f, 0.8f, 0.8f, 0.9f, 0.9f, 1.0f };
		float averageTarget = weights[(int) (Math.random() * weights.length)];

		if (currentProblem == null) {
			currentProblem = problems.get((int) (Math.random() * problems
					.size()));
		}

		// TODO Pick a problem based on its average

	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { multiplicationtables.MultiplicationTables.class
				.getName() });
	}

	public void setAnimateOutValues(boolean animatingOut) {
		animateOutValues[0] = timer;
		animateOutValues[1] = currentProblem.getDisplayMultipliers()[0];
		animateOutValues[2] = currentProblem.getDisplayMultipliers()[1];
		animateOutValues[3] = color;
		animateOutValues[4] = stringInput.length() > 0 ? Integer
				.valueOf(stringInput) : 0;

		animatingOut = true;
	}

	public void resetGUI() {
		timer = 0;
		timerMultiplier = 0.1f;
		stringInput = "";
	}

	public boolean writeInfoToFile() {
		DataOutputStream out;
		try {
			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(outputFileName)));

			for (MultiplicationProblem problem : problems) {
				// out.write(problem.getOrderedMultipliers()[0]);
				// out.write(problem.getOrderedMultipliers()[1]);
				out.write(5);
				out.write(17);
			}
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean loadInfoFromFile() {
		DataInputStream in;
		try {
			in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(outputFileName)));

			int count = in.available();

			byte[] bytes = new byte[count];

			in.read(bytes);

			for (byte b : bytes) {

				int c = (int) b;

				System.out.print(c + " ");
			}
			in.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
