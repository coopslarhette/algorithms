package csp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * CSP: Calendar Satisfaction Problem Solver
 * Provides a solution for scheduling some n meetings in a given
 * period of time and according to some unary and binary constraints
 * on the dates of each meeting.
 */
public class CSP {

    /**
     * Public interface for the CSP solver in which the number of meetings,
     * range of allowable dates for each meeting, and constraints on meeting
     * times are specified.
     *
     * @param nMeetings   The number of meetings that must be scheduled, indexed from 0 to n-1
     * @param rangeStart  The start date (inclusive) of the domains of each of the n meeting-variables
     * @param rangeEnd    The end date (inclusive) of the domains of each of the n meeting-variables
     * @param constraints Date constraints on the meeting times (unary and binary for this assignment)
     * @return A list of dates that satisfies each of the constraints for each of the n meetings,
     * indexed by the variable they satisfy, or null if no solution exists.
     */
    public static List<LocalDate> solve(int nMeetings, LocalDate rangeStart, LocalDate rangeEnd, Set<DateConstraint> constraints) {
        ArrayList<Meeting> meetings = buildMeetings(nMeetings, rangeStart, rangeEnd);
        ArrayList<LocalDate> assignments = new ArrayList<>();
        for (int i = 0; i < nMeetings; i++) {
            assignments.add(null);
        }
        return backtracking(constraints, meetings, assignments, 0);
    }


    private static ArrayList<LocalDate> backtracking(Set<DateConstraint> constraints, ArrayList<Meeting> meetings, ArrayList<LocalDate> assignments, int index) {
        ArrayList<LocalDate> result = new ArrayList<>();
        if (!assignments.contains(null) && checkConstraints(assignments, constraints)) {
            return assignments;
        }
        Meeting newMeeting = meetings.get(index);
        for (LocalDate currDate : newMeeting.dateRange) {
            assignments.set(index, currDate);
            if (checkConstraints(assignments, constraints)) {
                result = backtracking(constraints, meetings, assignments, index++);
                if (result != null) {
                    return result;
                }
            }
            assignments.set(index, null);
        }
        return null;
    }

    private static ArrayList<Meeting> buildMeetings(int nMeetings, LocalDate rangeStart, LocalDate rangeEnd) {
        ArrayList<Meeting> result = new ArrayList<>();
        for (int i = 0; i < nMeetings; i++) {
            result.add(new Meeting(rangeStart, rangeEnd));
        }

        return result;
    }


    /**
     * Checks to see if a CSP solution satisfies its constraints.
     *
     * @param assignments Assignments to check consistency of.
     * @param constraints Constraints used to check assignments.
     * @return True if CSP is constraint-consistent, false otherwise.
     */
    private static boolean checkConstraints(ArrayList<LocalDate> assignments, Set<DateConstraint> constraints) {
        System.out.println("constraints being checked");
        for (DateConstraint d : constraints) {
            System.out.println("assignments: " + assignments.toString());
            if (d.arity() == 2) {
                BinaryDateConstraint newConstraint = (BinaryDateConstraint) d;
                if (assignments.get(newConstraint.L_VAL) != null && assignments.get(newConstraint.R_VAL) != null) {
                    if (!checkDateConsistency(assignments.get(newConstraint.L_VAL), assignments.get(newConstraint.R_VAL), d)) {
                        return false;
                    }
                }
            } else {
                UnaryDateConstraint newConstraint = (UnaryDateConstraint) d;
                if (assignments.get(newConstraint.L_VAL) != null) {
                    if (!checkDateConsistency(assignments.get(newConstraint.L_VAL), newConstraint.R_VAL, d)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks consistency of two dates with given constraint.
     * @param leftDate
     * @param rightDate
     * @param constraint
     * @return
     */
    private static boolean checkDateConsistency(LocalDate leftDate, LocalDate rightDate, DateConstraint constraint) {
        switch (constraint.OP) {
            case "==":
                if (leftDate.isEqual(rightDate)) return true;
                break;
            case "!=":
                if (!leftDate.isEqual(rightDate)) return true;
                break;
            case ">":
                if (leftDate.isAfter(rightDate)) return true;
                break;
            case "<":
                if (leftDate.isBefore(rightDate)) return true;
                break;
            case ">=":
                if (leftDate.isAfter(rightDate) || leftDate.isEqual(rightDate)) return true;
                break;
            case "<=":
                if (leftDate.isBefore(rightDate) || leftDate.isEqual(rightDate)) return true;
                break;
        }

        return false;
    }

    private static class Meeting {
        ArrayList<LocalDate> dateRange;

        Meeting(LocalDate rangeStart, LocalDate rangeEnd) {
            dateRange = new ArrayList<>();
            while (rangeStart.isBefore(rangeEnd) || rangeStart.isEqual(rangeEnd)) {
                dateRange.add(rangeStart);
                rangeStart = rangeStart.plusDays(1);
            }
        }
    }

}
