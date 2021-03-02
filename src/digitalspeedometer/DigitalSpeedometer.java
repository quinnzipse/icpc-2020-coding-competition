package digitalspeedometer;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DigitalSpeedometer {
    // t_f < t_r
    private final double t_f;
    private final double t_r;
    private final ArrayList<Double> oldSpeeds;

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        var speedometer = new DigitalSpeedometer(scanner.nextDouble(), scanner.nextDouble());
        while (scanner.hasNext()) {
            speedometer.solve(scanner.nextDouble());
        }

    }

    public void solve(double current_speed) {
        final int j = (int) Math.floor(current_speed) + 1;
        final int i = (int) Math.floor(current_speed);
        final double falling_thresh = i + t_f;
        final double rising_thresh = i + t_r;
        int displayed_speed = -1;

        // Any sensed speed, 0<s<1, must display as 1 because any non-zero speed, no matter how small,
        // must display as non-zero to indicate that the vehicle is in motion.
        if (0 < current_speed && current_speed < 1) {
            System.out.println("1");
            oldSpeeds.add(current_speed);
            return;
        }

        if (i < current_speed && current_speed < falling_thresh) {
            // When s falls between i and i+tf, s is displayed as i.
            displayed_speed = i;
        } else if (rising_thresh < current_speed && current_speed < j) {
            // When s falls between i+tr and j, s is displayed as j.
            displayed_speed = j;
        } else if (falling_thresh < current_speed && current_speed < rising_thresh) {
            // When s falls between i+tf and i+tr, s is displayed as i if the most recent preceding value for s outside
            // of range [i+tf,i+tr] is less than i+tr, and s is displayed as j if the most recent preceding value for s
            // outside of range [i+tf,i+tr] is greater than i+tr.


//            double old_speed = 0;
//            for (int x = oldSpeeds.size() - 1; x >= 0; x--) {
//                old_speed = oldSpeeds.get(x);
//                if (old_speed <= i + t_f || i + t_r <= old_speed) {
//                    break;
//                }
//            }

            var valid_speeds = oldSpeeds.stream().filter(speed -> speed < falling_thresh || speed > rising_thresh).collect(Collectors.toList());
            double old_speed = valid_speeds.get(valid_speeds.size() - 1);
//            System.err.println(old_speed);

            if (old_speed > rising_thresh) {
                displayed_speed = j;
            } else {
                displayed_speed = i;
            }
        }


        System.out.println(displayed_speed);
        oldSpeeds.add(current_speed);
    }

    public DigitalSpeedometer(double t_f, double t_r) {
        this.t_f = t_f;
        this.t_r = t_r;
        this.oldSpeeds = new ArrayList<>();
    }
}
