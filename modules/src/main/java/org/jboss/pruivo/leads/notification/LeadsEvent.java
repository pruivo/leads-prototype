package org.jboss.pruivo.leads.notification;

import java.io.Serializable;

/**
 * Stores the information that is sent to the coordinator when the keys is accepted. It contains the line number in
 * which the words "free" and "beer" appear in the same line.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class LeadsEvent implements Serializable {

    private final int lineNumber;

    public LeadsEvent(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
