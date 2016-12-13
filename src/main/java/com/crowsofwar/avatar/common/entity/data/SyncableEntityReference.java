package com.crowsofwar.avatar.common.entity.data;

import com.crowsofwar.avatar.common.data.CachedEntity;
import com.crowsofwar.avatar.common.entity.AvatarEntity;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

/**
 * Like {@link CachedEntity}, but allows access to the server/client
 * counterparts of an entity, on both sides.
 * <p>
 * Designed for use to have 2 entities having synced references to each other.
 * Uses DataManager to sync the entities' IDs and then performs lookup/caching.
 * 
 * @author CrowsOfWar
 */
public class SyncableEntityReference<T extends AvatarEntity> {
	
	private final AvatarEntity using;
	private final DataParameter<Integer> sync;
	private final CachedEntity<T> cache;
	
	/**
	 * Create an entity reference.
	 * 
	 * @param entity
	 *            The entity that is USING the reference, usually
	 *            <code>this</code>. Not the entity being referenced
	 * @param cls
	 *            Class of who instantiates this. May not necessarily be
	 *            <code>entity.getClass()</code> in the case of subclasses
	 */
	public <E extends AvatarEntity> SyncableEntityReference(E entity, Class<E> cls) {
		this.using = entity;
		this.sync = EntityDataManager.createKey(cls, DataSerializers.VARINT);
		this.cache = new CachedEntity<T>(-1);
		entity.getDataManager().register(sync, -1);
	}
	
	public T getEntity() {
		// Cache may have an incorrect id; other side could have changed
		// dataManager id, but not the cached entity id.
		cache.setEntityId(using.getDataManager().get(sync));
		return cache.getEntity(using.worldObj);
	}
	
	public void setEntity(T entity) {
		cache.setEntity(entity);
		using.getDataManager().set(sync, cache.getEntityId());
	}
	
}
