package org.luis.basic.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

@MappedSuperclass
public abstract class BaseEntity implements java.io.Serializable {

	private static final long serialVersionUID = 4655172877720864152L;

	/**
	 * 映射成主键，key生成策略为AUTO->IDENTITY AUTO:支持Mysql,SqlServer,Oracle 但Oracle还需要@SequenceGenerator
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "NGBF_SEQUENCE")
	@SequenceGenerator(name = "NGBF_SEQUENCE", sequenceName = "NGBF_SEQ", allocationSize = 1, initialValue = 1)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean isNewEntity() {
		if (getId() == null || getId().intValue() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (id == null || id.intValue() == 0) {
			return false;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final BaseEntity other = (BaseEntity) obj;
		if (other.id == null || other.id.intValue() == 0) {
			return false;
		}
		return id.equals(other.id);
	}

	/**
	 * 祛除字符串前后的空格,当字符串的数据库配置是CHAR(n)的时候，从数据库会带出多余的空格，需要祛除
	 * @param s
	 * @return
	 */
	protected static String trim(String s) {
		if (s != null) {
			return s.trim();
		}
		return s;
	}

}
