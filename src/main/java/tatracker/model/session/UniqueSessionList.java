//@@author Chuayijing

package tatracker.model.session;

import static java.util.Objects.requireNonNull;
import static tatracker.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Duration;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import tatracker.model.session.exceptions.DuplicateSessionException;
import tatracker.model.session.exceptions.SessionNotFoundException;

/**
 * A list of sessions that enforces uniqueness between its elements and does not allow nulls.
 * A session is considered unique by comparing using {@code Session#isSameSession(Session)}.
 * As such, adding and updating of sessions uses Session#isSameSession(Session)
 * for equality so as to ensure that the session being added or updated is
 * unique in terms of identity in the UniqueSessionList. However,
 * the removal of a session uses Session#equals(Object) so as to ensure that
 * the session with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Session#isSameSession(Session)
 */
public class UniqueSessionList implements Iterable<Session> {

    private final ObservableList<Session> internalList = FXCollections.observableArrayList();
    private final ObservableList<Session> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent session as the given argument.
     */
    public boolean contains(Session toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameSession);
    }

    /**
     * Returns the size of the UniqueSessionList.
     */
    public int size() {
        return internalList.size();
    }

    /**
     * Returns the session at the given index.
     */
    public Session get(int n) {
        return internalList.get(n);
    }

    /**
     * Adds a session to the list.
     * The session must not already exist in the list.
     */
    public void add(Session toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateSessionException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the session {@code target} in the list with {@code editedSession}.
     * {@code target} must exist in the list.
     * The session identity of {@code editedSession} must not be the same as another existing session in the list.
     */
    public void setSession(Session target, Session editedSession) {
        requireAllNonNull(target, editedSession);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new SessionNotFoundException();
        }

        if (!target.isSameSession(editedSession) && contains(editedSession)) {
            throw new DuplicateSessionException();
        }

        internalList.set(index, editedSession);
    }

    /**
     * Removes the equivalent session from the list.
     * The session must exist in the list.
     */
    public void remove(Session toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new SessionNotFoundException();
        }
    }

    /**
     * Removes the session of the given index from the list.
     * The session must exist in the list.
     */
    public void remove(int n) {
        if (n < 0 || n > internalList.size()) {
            throw new SessionNotFoundException();
        }

        internalList.remove(n);
    }

    public void setSessions(UniqueSessionList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code sessions}.
     * {@code sessions} must not contain duplicate sessions.
     */
    public void setSessions(List<Session> sessions) {
        requireAllNonNull(sessions);
        if (!sessionsAreUnique(sessions)) {
            throw new DuplicateSessionException();
        }

        internalList.setAll(sessions);
    }

    /**
     * Returns the session list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Session> asUnmodifiableObservableList() {

        FXCollections.sort(internalList, Comparator.comparing(Session::getDate)
                                                    .thenComparing(Session::getStartDateTime)
                                                    .thenComparing(Session::getEndDateTime));
        return internalUnmodifiableList;
    }

    /**
     * Returns all sessions of type {@code type}.
     * @param type The type of session to return.
     */
    public UniqueSessionList getSessionsOfType(SessionType type) {
        UniqueSessionList filteredList = new UniqueSessionList();
        filteredList.setSessions(internalList.filtered(s -> s.getSessionType() == type));
        return filteredList;
    }

    /**
     * Returns all sessions of module code {@code code}.
     * @param code The module code of session to return.
     */
    public UniqueSessionList getSessionsOfModuleCode(String code) {
        UniqueSessionList filteredList = new UniqueSessionList();
        filteredList.setSessions(internalList.filtered(s -> s.getModuleCode().equals(code)));
        return filteredList;
    }

    /**
     * @return the total duration of all sessions in this session list.
     */
    public Duration getTotalDuration() {
        return internalList.stream()
                .map(Session::getDurationToNearestHour)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public Iterator<Session> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueSessionList // instanceof handles nulls
                && internalList.equals(((UniqueSessionList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code sessions} contains only unique sessions.
     */
    private boolean sessionsAreUnique(List<Session> sessions) {
        for (int i = 0; i < sessions.size() - 1; i++) {
            for (int j = i + 1; j < sessions.size(); j++) {
                if (sessions.get(i).isSameSession(sessions.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
