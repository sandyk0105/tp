package seedu.address.model.person;

import java.util.Objects;

/**
 * A class meant to group all relevant details of an appointment together.
 *
 * @param <Id> A generic type representing the ID type.
 */
public class Appointment<Id> {
    private Id patientId;
    private Id doctorId;
    private String remarks;

    /**
     * Creates an appointment instance associated with the specified patient, doctor with the given remarks.
     *
     * @param patientId Id of patient.
     * @param doctorId Id of doctor.
     * @param remarks Remarks given by the doctor for the appointment or patient.
     */
    public Appointment(Id patientId, Id doctorId, String remarks) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.remarks = remarks;
    }

    public Id getPatientId() {
        return patientId;
    }

    public Id getDoctorId() {
        return doctorId;
    }

    public String getRemarks() {
        return remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Appointment<?> appointment = (Appointment<?>) o; // Typecast to Appointment

        // Compare key and value
        return Objects.equals(patientId, appointment.patientId)
                && Objects.equals(doctorId, appointment.doctorId)
                && Objects.equals(remarks, appointment.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, doctorId, remarks);
    }

    @Override
    public String toString() {
        return "Appointment: " + getPatientId() + " (patient id) with " + getDoctorId() + " (doctor id). "
                + "Remarks: " + getRemarks();
    }
}
