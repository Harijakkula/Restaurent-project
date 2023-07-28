public abstract class Person {
    private static Long PERSON_ID = 0L;

    private final Long id;
    private final String name;
    private String phoneNumber;
    private String email;

    public Person(String name) {
        this.name = name;
        this.id = ++PERSON_ID;
    }

    public Person(String name, String phoneNumber, String email) {
        this(name);
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
