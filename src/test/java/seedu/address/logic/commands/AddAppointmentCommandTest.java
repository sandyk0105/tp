package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Doctor;
import seedu.address.model.person.Id;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
public class AddAppointmentCommandTest {
    private final LocalDateTime defaultTime = LocalDateTime.of(2024, 12, 31, 12, 0);
    private final String defaultRemark = "";
    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddAppointmentCommand(null, null, null, null));
    }
    @Test
    public void execute_appointmentAcceptedByModel_addSuccessful() throws Exception {
        AddAppointmentCommandTest.ModelStubAcceptingAppointmentAdded modelStub = new AddAppointmentCommandTest
                .ModelStubAcceptingAppointmentAdded();
        Person validPatient = new PersonBuilder().buildPatient();
        Person validDoctor = new PersonBuilder().buildDoctor();

        modelStub.addPersonToList(validPatient);
        modelStub.addPersonToList(validDoctor);

        CommandResult commandResult = new AddAppointmentCommand(defaultTime, validPatient.getId(),
                validDoctor.getId(), defaultRemark).execute(modelStub);

        assertEquals(AddAppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                commandResult.getFeedbackToUser());
        String expectedAppointments = String.format("All appointments for you in the database:\n"
                + "Appointment: Id{id=%1$d, role=Patient} (patient id) "
                + "with Id{id=%2$d, role=Doctor} (doctor id). Remarks: "
                + "\n", validPatient.getId().getIdValue(), validDoctor.getId().getIdValue());

        assertEquals(expectedAppointments, validDoctor.getAllAppointments());
    }
    @Test
    public void execute_duplicateAppointment_throwsCommandException() {
        Patient validPatient = new PersonBuilder().buildPatient();
        Doctor validDoctor = new PersonBuilder().buildDoctor();
        validPatient.addAppointment(defaultTime, validPatient.getId(), validDoctor.getId(), defaultRemark);
        AddAppointmentCommand addAppointmentCommand = new AddAppointmentCommand(defaultTime,
                validPatient.getId(), validDoctor.getId(), defaultRemark);
        AddAppointmentCommandTest.ModelStub modelStub = new AddAppointmentCommandTest
                .ModelStubWithAppointment(validPatient, validDoctor);

        assertThrows(CommandException.class, AddAppointmentCommand
                .MESSAGE_DUPLICATE_APPOINTMENT, () -> addAppointmentCommand.execute(modelStub));
    }
    /**
     * A default model stub that have all methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return null;
        }

        @Override
        public ObservableList<Person> getFilteredPersonById(Id id) {
            return null;
        }

        @Override
        public Patient getFilteredPatientById(ObservableList<Person> allPersons, Id id) {
            return null;
        }

        @Override
        public Doctor getFilteredDoctorById(ObservableList<Person> allPersons, Id id) {
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
        }
    }
    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithAppointment extends AddAppointmentCommandTest.ModelStub {
        private final Patient patient;
        private final Doctor doctor;

        ModelStubWithAppointment(Patient patient, Doctor doctor) {
            requireNonNull(patient);
            requireNonNull(doctor);
            this.patient = patient;
            this.doctor = doctor;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return person.isSamePerson(patient) || person.isSamePerson(doctor);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return javafx.collections.FXCollections.observableArrayList(patient, doctor);
        }

        @Override
        public Patient getFilteredPatientById(ObservableList<Person> allPersons, Id id) {
            return patient.getId().equals(id) ? patient : null;
        }

        @Override
        public Doctor getFilteredDoctorById(ObservableList<Person> allPersons, Id id) {
            return doctor.getId().equals(id) ? doctor : null;
        }
    }

    /**
     * A Model stub that always accept the appointment being added.
     */
    public class ModelStubAcceptingAppointmentAdded extends AddAppointmentCommandTest.ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        private final ArrayList<Person> personList = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public Patient getFilteredPatientById(ObservableList<Person> allPersons, Id id) {
            // Search for a patient with the specified ID
            for (Person person : allPersons) {
                if (person.getId().equals(id) && person instanceof Patient) {
                    return (Patient) person;
                }
            }
            return null;
        }

        @Override
        public Doctor getFilteredDoctorById(ObservableList<Person> allPersons, Id id) {
            // Search for a doctor with the specified ID
            for (Person person : allPersons) {
                if (person.getId().equals(id) && person instanceof Doctor) {
                    return (Doctor) person;
                }
            }
            return null;
        }
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return javafx.collections.FXCollections.observableArrayList(personList);
        }
        public void addPersonToList(Person person) {
            personList.add(person);
        }
    }

}
