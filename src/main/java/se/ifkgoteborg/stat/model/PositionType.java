package se.ifkgoteborg.stat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import se.ifkgoteborg.stat.model.enums.MajorVerticalAlignment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="position_type")
public class PositionType {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy="positionType")
	@JsonIgnore
	private List<Position> positions = new ArrayList<Position>();

	@Enumerated(EnumType.STRING)
	private MajorVerticalAlignment alignment;
	
	private PositionType() {}
	
	public PositionType(String name, MajorVerticalAlignment alignment) {
		this.name = name;
		this.alignment = alignment;		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MajorVerticalAlignment getAlignment() {
		return alignment;
	}

	public void setAlignment(MajorVerticalAlignment alignment) {
		this.alignment = alignment;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PositionType other = (PositionType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
}
