package se.ifkgoteborg.stat.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import se.ifkgoteborg.stat.model.enums.MinorHorizontalAlignment;
import se.ifkgoteborg.stat.model.enums.MinorVerticalAlignment;
import se.ifkgoteborg.stat.model.enums.Side;

@Entity
@Table(name="position")
public class Position {
	
	@Id
	@GeneratedValue
	private Long id;

	private String name;
	
	private String code;
	
	@Enumerated(value=EnumType.STRING)
	private Side side;

	@ManyToOne
	private PositionType positionType;

	@Enumerated(value=EnumType.STRING)
	private MinorVerticalAlignment minorVerticalAlignment = MinorVerticalAlignment.NEUTRAL;

	@Enumerated(value=EnumType.STRING)
	private MinorHorizontalAlignment minorHorizontalAlignment = MinorHorizontalAlignment.NEUTRAL;
	
	public Position() {}
	
	public Position(String name, String code, Side side, PositionType positionType) {
		this.name = name;
		this.code = code;
		this.side = side;
		this.positionType = positionType;
	}
	
	public Position(String name, String code, Side side, PositionType positionType, MinorVerticalAlignment minorVerticalAlignment) {
		this.name = name;
		this.code = code;
		this.side = side;
		this.positionType = positionType;
		this.minorVerticalAlignment = minorVerticalAlignment;
	}

	public Position(String name, String code, Side side, PositionType positionType, MinorHorizontalAlignment minorHorizontalAlignment) {
		this.name = name;
		this.code = code;
		this.side = side;
		this.positionType = positionType;
		this.minorHorizontalAlignment = minorHorizontalAlignment;
	}

	public Position(String name, String code, Side side, PositionType positionType, MinorVerticalAlignment minorVerticalAlignment, MinorHorizontalAlignment minorHorizontalAlignment) {
		this.name = name;
		this.code = code;
		this.side = side;
		this.positionType = positionType;
		this.minorVerticalAlignment = minorVerticalAlignment;
		this.minorHorizontalAlignment = minorHorizontalAlignment;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Side getSide() {
		return side;
	}
	public void setSide(Side side) {
		this.side = side;
	}

	public PositionType getPositionType() {
		return positionType;
	}

	public void setPositionType(PositionType positionType) {
		this.positionType = positionType;
	}
	
	

	public MinorVerticalAlignment getMinorVerticalAlignment() {
		return minorVerticalAlignment;
	}

	public void setMinorVerticalAlignment(
			MinorVerticalAlignment minorVerticalAlignment) {
		this.minorVerticalAlignment = minorVerticalAlignment;
	}

	public MinorHorizontalAlignment getMinorHorizontalAlignment() {
		return minorHorizontalAlignment;
	}

	public void setMinorHorizontalAlignment(
			MinorHorizontalAlignment minorHorizontalAlignment) {
		this.minorHorizontalAlignment = minorHorizontalAlignment;
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
		Position other = (Position) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
	
}
