/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package markovhigher;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
/**
 *
 * @author Marta Albecka, Pawel Karpinski, Piotr Lecki
 */
public class NotesCollection extends Vector<Note> {

    List<Note> startedNotes;
    SortedSet<Note> notesInTheSameTick;
    long tick = 0;
    long prevTick;
    Note prevNote;

    public NotesCollection() {}
    
    /**
     * Creates an instance of NotesCollection
     * @param file file which contains notes which will be kept in the NotesCollection
     */
    public NotesCollection(File file) {
        super();
        Sequencer sequencer = null;
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence sequence = MidiSystem.getSequence(file);


            startedNotes = new ArrayList<>();
            int trackNumber = 0;

            for (Track track :  sequence.getTracks()) {
                notesInTheSameTick = new TreeSet<>();
                prevTick = 0;
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    tick = event.getTick();
                    MidiMessage message = event.getMessage();

                    if (message instanceof ShortMessage) {
                        ShortMessage shortMessage = (ShortMessage) message;

                        if (prevNote != null && tick != prevNote.getTick() && !notesInTheSameTick.isEmpty())
                            newTick();
                        //counting duration and distances (from start to start) and adding them to notes collection
                        switch (Note.getMessageType(shortMessage)) {
                            case NOTE_START:
                                addNote(trackNumber, shortMessage);
                                break;
                            case NOTE_STOP:
                                endNote(shortMessage);
                                break;
                        }
                    } 
                }
                if(!startedNotes.isEmpty())
                    throw new InvalidMidiDataException("Not every note ended");
                trackNumber++;
            }
        } catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Creates an instance of NotesCollection
     * @param fileName string with name of file which contains notes which will be kept in the NotesCollection 
     */
    public NotesCollection(String fileName) {
        this(new File(fileName));
    }

    /**
     * adds a note from the previous tick with the higher key value (last in the sorted set)
     */
    private void newTick() {
        Note noteToAdd = notesInTheSameTick.last();
        startedNotes.add(noteToAdd);
        this.add(noteToAdd);
        notesInTheSameTick.clear();
        prevTick = noteToAdd.getTick();
    }
    /**
     * Adds new note to the sorted set and sets it as previous note
     * @param trackNumber track number of the new note
     * @param shortMessage ShortMessage of the new note
     * @throws InvalidMidiDataException 
     */
    private void addNote(int trackNumber, ShortMessage shortMessage) throws InvalidMidiDataException {
        Note thisNote = new Note(trackNumber, tick, shortMessage);
        thisNote.setDistanceFromPrevious(tick - prevTick);
        prevNote = thisNote;
        notesInTheSameTick.add(thisNote);
    }
    /**
     * Removes the given note from the sorted set
     * @param shortMessage information about the note that ended
     */
    private void endNote(ShortMessage shortMessage) {
        Note startingNote = findStartingNote(shortMessage);
        if(startingNote != null) {
            startingNote.setDuration(tick - startingNote.getTick());
            startedNotes.remove(startingNote);
        }
    }
   
    /**
     * Creates a sublist of NotesCollection
     * @param fromIndex index of the first element that will be included in the sublist
     * @param toIndex index of the last element that will be included in the sublist
     * @return sublist
     */
    public NotesCollection sublist(int fromIndex, int toIndex) {
        NotesCollection ret = new NotesCollection();
        for(int i = fromIndex; i <= toIndex; i++) {
            ret.add(this.elementAt(i));
        }
        return ret;
    }
    /**
     * Finds a note in the startedNotes list to a given end
     * @param endingNoteMessage information about the note
     * @return 
     */
    public Note findStartingNote(ShortMessage endingNoteMessage) {
        for(Note note : startedNotes) {
            if(note.getKey() == Note.getKey(endingNoteMessage))
                return note;
        }
        return null;
    }
    /**
     * Overrides method elementAt to compare elements with compareTo method no the default one,
     * @param index index of the element to be returned
     * @return element at >index<
     */
    @Override
    public synchronized Note elementAt(int index) {
        for (Note note: this) {
            if(note.compareTo(super.elementAt(index)) == 0)
                return note;
        }
        return null;
    }
}
