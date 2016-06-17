package shortcuts;

public class Shortcut {
	private String priority;
	private String name;
	private String location;

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public boolean equals(Object arg0) {
		Shortcut file = (Shortcut) arg0;
		if (file.getName().equals(this.name)
				&& file.getLocation().equals(this.location))
			return true;
		else
			return false;
	}

}
