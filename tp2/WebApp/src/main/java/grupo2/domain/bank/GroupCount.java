package grupo2.domain.bank;

import grupo2.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "GroupsCount")
public class GroupCount extends PersistentEntity {
	@Column(name="GroupId")
	private String groupId;
	@Column(name="Ammount")
	private int ammount;
	
	GroupCount() {
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getAmmount() {
		return ammount;
	}

	public void setAmmount(int ammount) {
		this.ammount = ammount;
	}


		
}
