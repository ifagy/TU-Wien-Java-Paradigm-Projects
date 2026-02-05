/**
 * Represents a collection of Organism objects, implemented as a singly
 * linked list.
 * Uses a static nested class OrganismNode to form the list.
 */
@Author("Ozan")
public class Set {
    private OrganismNode head;
    private int size = 0;

    @Author("Ozan")
    @PostCondition("An instance of Set is created. The 'head' is null.")
    /**
     * Initializes an empty Set.
     */
    public Set() {
        head = null;
    }

@Author("Ozan")
// as statically nested inner class
    private static class OrganismNode {
        private Organism org;
        private OrganismNode next;


    @Author("Ozan")
    @PreCondition("The provided Organism instance org is valid and not null.")
    @PostCondition("An instance of OrganismNode is created. @org is set to the provided Organism, and @next is set to null.")
    /**
     * Constructor for an OrganismNode.
     * @param org The Organism to store in this node.
     */
        public OrganismNode(Organism org) {
            this.org = org;
            this.next = null;
        }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns the reference stored in the 'next' field." +
            "The state of the object remains unchanged.")
    /**
     * Retrieves the next node in the list.
     * @return The next OrganismNode, or null if this is the last node.
     */
        public OrganismNode getNext() {
            return next;
        }

    @Author("Ozan")
    @PreCondition("-The object @this must be valid." +
            "The provided OrganismNode instance 'next' is valid or null.")
    @PostCondition("The 'next' field is set to the provided 'next' parameter.")
    /**
     * Sets the reference to the next node.
     * @param next The OrganismNode to set as the next node.
     */
        public void setNext(OrganismNode next) {
            this.next = next;
        }


    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns the Organism stored in the 'org' field." +
            "The state of the object remains unchanged.")
    /**
     * Retrieves the Organism stored in this node.
     * @return The Organism object.
     */
        public Organism getOrganism() {
            return org;
        }
    }


    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided Organism instance @org is valid and not null.")
    @PostCondition("The new Organism is added as the last element of the list." +
            "The size is incremented by 1.")
    /**
     * Adds a new Organism to the end of the list.
     * @param org The Organism to add.
     */
    public void addOrganism(Organism org) {
        if (head == null) {
            head = new OrganismNode(org);
            size++;
        }else{
            OrganismNode temp = head;
            while(temp.getNext() != null){
                temp = temp.getNext();
            }
            temp.setNext(new OrganismNode(org));
            size++;
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns the current value of the 'size' field." +
            "The state of the object remains unchanged.")
    /**
     * Returns the number of Organisms in the set.
     * @return The current size of the set.
     */
    public int size() {
        return size;
    }


    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("If index is less than 0 or greater than or equal to 'size', returns null. " +
            "Otherwise, returns the Organism at given index." + "The state of the object remains unchanged.")
    /**
     * Returns the Organism at the specified index.
     * @param index The 0-based index of the Organism to retrieve.
     * @return The Organism at the given index, or null if the index is out of bounds.
     */
    public Organism getOrganism(int index) {
        if(index < 0 || index >= size){return null;}
        OrganismNode temp = head;

        for(int i = 0; i < index; i++){
            temp = temp.getNext();
        }
        return temp.getOrganism();
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("The {@code endOfDay()} method is called on every Organism currently in the set." +
            "Any Organism that returns false from its {@code isActive()} method after {@code endOfDay()} is removed from the set." +
            "The 'size' is decreased by the number of removed inactive Organisms.")
    /**
     * Iterates through all Organisms, calls their endOfDay method, and removes any
     * Organism that becomes inactive.
     */
    public void manageEndOfDay(){
        OrganismNode current = head;
        OrganismNode prev = null;
        while(current != null) {
            current.getOrganism().endOfDay();

            if (!current.getOrganism().isActive()) {
                if (prev == null) {
                    head = current.getNext();
                    size--;
                } else {
                    prev.setNext(current.getNext());
                    size--;
                }
                current = current.getNext();
            } else {
                prev = current;
                current = current.getNext();
            }
        }
    }
}


