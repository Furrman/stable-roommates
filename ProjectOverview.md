# Introduction #

The Stable Roommates algorithm (SR) is an extension of the Stable Marriage algorithm, made popular by pairing medical students with fellowship placements.  SR is a non-bipartite matching algorithm.  Given a group of elements and a list of preferences of the other elements in the group, the goal is to figure out a stable matching, such that no two elements prefer two others over themselves.

More succinctly:
A matching M in an instance of SR is stable if there are no two participants x and y, each of whom prefers the other to his partner in M. Such a pair is said to block M, or to be a blocking pair with respect to M.

There are different types of matches.  A super-stable matching is one in which the end result has one, and only one, choice for each element.  A weakly-stable matching is one in which the end results has at least one element with more than one element in its preference list.

It has been proved that finding the minimum or maximum weakly-stable matching is an NP-complete problem.

# Ties and Incomplete Lists #

The original SR algorithm required that every element have a preference for every other element (not including itself).  It also required each element to strictly prefer each other element.

In the real world, things are not usually that clear cut.  Sometimes an element, not mater was, cannot be matched with another.  This program will allow for incomplete preference lists to account for this situation.

Also, sometimes an element prefers another element just as much as yet another one.  This program will allow for ties in the preference lists to account for this situation.


# Project Goal #

The goal of this project is to create an implementation that meets the descriptions above in the Java programming language.  It will be written such that any class can be matched.  The user will also be able to specify if a stable matching needs to be returned, or if a weak matching is acceptable.

# Program Use #

Once complete, this program can be used in many applications.  You will only have to create your preferences lists and let the program run.  Some examples are tournament pairings, work partners, etc.