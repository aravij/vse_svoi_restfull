package com.vsesvoi.entity

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "community")
class Community(
    @Column(name = "name", nullable = false, length = 50)
    var name: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @ManyToMany(mappedBy = "communities", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var users: MutableSet<User> = mutableSetOf()

    @OneToMany(mappedBy = "community", cascade = [CascadeType.ALL], orphanRemoval = true)
    var wishes: MutableSet<Wish> = mutableSetOf()

    fun add(user: User) {
        users.add(user)
        user.communities.add(this)
    }

    fun remove(user: User) {
        users.remove(user)
        user.communities.remove(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Community

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

}