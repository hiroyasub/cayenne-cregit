begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|ObjEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|ObjectIdQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|PropertyDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|PropertyUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Various utils for processing persistent objects and their properties  *<p>  *<i>DataObjects and Primary Keys: All methods that allow to extract primary  * key values or use primary keys to find objects are provided for convenience.  * Still the author's belief is that integer sequential primary keys are  * meaningless in the object model and are pure database artifacts. Therefore  * relying heavily on direct access to PK provided via this class (or other such  * Cayenne API) is not a clean design practice in many cases, and sometimes may  * actually lead to security issues.</i>  *</p>  *   * @since 3.1 its predecessor was called DataObjectUtils  */
end_comment

begin_class
specifier|public
class|class
name|Cayenne
block|{
comment|/**      * A special property denoting a size of the to-many collection, when      * encountered at the end of the path</p>      */
specifier|final
specifier|static
name|String
name|PROPERTY_COLLECTION_SIZE
init|=
literal|"@size"
decl_stmt|;
comment|/**      * Returns mapped ObjEntity for object. If an object is transient or is not      * mapped returns null.      */
specifier|public
specifier|static
name|ObjEntity
name|getObjEntity
parameter_list|(
name|Persistent
name|p
parameter_list|)
block|{
return|return
operator|(
name|p
operator|.
name|getObjectContext
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|p
operator|.
name|getObjectContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|p
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Returns class descriptor for the object or null if the object is not      * registered with an ObjectContext or descriptor was not found.      */
specifier|public
specifier|static
name|ClassDescriptor
name|getClassDescriptor
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
name|ObjectContext
name|context
init|=
name|object
operator|.
name|getObjectContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns property descriptor for specified property.      *       * @param propertyName      *            path to the property      * @return property descriptor,<code>null</code> if not found      */
specifier|public
specifier|static
name|PropertyDescriptor
name|getProperty
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|ClassDescriptor
name|descriptor
init|=
name|getClassDescriptor
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|descriptor
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|descriptor
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
comment|/**      * Returns a value of the property identified by a property path. Supports      * reading both mapped and unmapped properties. Unmapped properties are      * accessed in a manner consistent with JavaBeans specification.      *<p>      * Property path (or nested property) is a dot-separated path used to      * traverse object relationships until the final object is found. If a null      * object found while traversing path, null is returned. If a list is      * encountered in the middle of the path, CayenneRuntimeException is thrown.      * Unlike {@link DataObject#readPropertyDirectly(String)}, this method will resolve an      * object if it is HOLLOW.      *<p>      * Examples:      *</p>      *<ul>      *<li>Read this object property:<br>      *<code>String name = (String)Cayenne.readNestedProperty(artist, "name");</code>      *<br>      *<br>      *</li>      *<li>Read an object related to this object:<br>      *<code>Gallery g = (Gallery)Cayenne.readNestedProperty(paintingInfo, "toPainting.toGallery");</code>      *<br>      *<br>      *</li>      *<li>Read a property of an object related to this object:<br>      *<code>String name = (String)Cayenne.readNestedProperty(painting, "toArtist.artistName");</code>      *<br>      *<br>      *</li>      *<li>Read to-many relationship list:<br>      *<code>List exhibits = (List)Cayenne.readNestedProperty(painting, "toGallery.exhibitArray");</code>      *<br>      *<br>      *</li>      *<li>Read to-many relationship in the middle of the path:<br>      *<code>List&lt;String&gt; names = (List&lt;String&gt;)Cayenne.readNestedProperty(artist, "paintingArray.paintingName");</code>      *<br>      *<br>      *</li>      *</ul>      */
specifier|public
specifier|static
name|Object
name|readNestedProperty
parameter_list|(
name|Object
name|o
parameter_list|,
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|else if
condition|(
name|o
operator|instanceof
name|DataObject
condition|)
block|{
return|return
operator|(
operator|(
name|DataObject
operator|)
name|o
operator|)
operator|.
name|readNestedProperty
argument_list|(
name|path
argument_list|)
return|;
block|}
if|else if
condition|(
name|o
operator|instanceof
name|Collection
argument_list|<
name|?
argument_list|>
condition|)
block|{
comment|// This allows people to put @size at the end of a property
comment|// path and be able to find out the size of a relationship.
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|equals
argument_list|(
name|PROPERTY_COLLECTION_SIZE
argument_list|)
condition|)
block|{
return|return
name|collection
operator|.
name|size
argument_list|()
return|;
block|}
comment|// Support for collection property in the middle of the path
name|Collection
argument_list|<
name|Object
argument_list|>
name|result
init|=
name|o
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
condition|?
operator|new
name|ArrayList
argument_list|<>
argument_list|()
else|:
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|item
range|:
name|collection
control|)
block|{
if|if
condition|(
name|item
operator|instanceof
name|DataObject
condition|)
block|{
name|DataObject
name|cdo
init|=
operator|(
name|DataObject
operator|)
name|item
decl_stmt|;
name|Object
name|rest
init|=
name|cdo
operator|.
name|readNestedProperty
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|rest
operator|instanceof
name|Collection
argument_list|<
name|?
argument_list|>
condition|)
block|{
comment|// We don't want nested collections. E.g.
comment|// readNestedProperty("paintingArray.paintingTitle")
comment|// should return
comment|// List<String>
name|result
operator|.
name|addAll
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|rest
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|add
argument_list|(
name|rest
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
if|if
condition|(
operator|(
literal|null
operator|==
name|path
operator|)
operator|||
operator|(
literal|0
operator|==
name|path
operator|.
name|length
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"the path must be supplied in order to lookup a nested property"
argument_list|)
throw|;
block|}
name|int
name|dotIndex
init|=
name|path
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|==
name|dotIndex
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"the path is invalid because it starts with a period character"
argument_list|)
throw|;
block|}
if|if
condition|(
name|dotIndex
operator|==
name|path
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"the path is invalid because it ends with a period character"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|-
literal|1
operator|==
name|dotIndex
condition|)
block|{
return|return
name|readSimpleProperty
argument_list|(
name|o
argument_list|,
name|path
argument_list|)
return|;
block|}
name|String
name|path0
init|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dotIndex
argument_list|)
decl_stmt|;
name|String
name|pathRemainder
init|=
name|path
operator|.
name|substring
argument_list|(
name|dotIndex
operator|+
literal|1
argument_list|)
decl_stmt|;
comment|// this is copied from the old code where the placement of a plus
comment|// character at the end of a segment of a property path would
comment|// simply strip out the plus. I am not entirely sure why this is
comment|// done. See unit test 'testReadNestedPropertyToManyInMiddle1'.
if|if
condition|(
literal|'+'
operator|==
name|path0
operator|.
name|charAt
argument_list|(
name|path0
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
condition|)
block|{
name|path0
operator|=
name|path0
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path0
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|Object
name|property
init|=
name|readSimpleProperty
argument_list|(
name|o
argument_list|,
name|path0
argument_list|)
decl_stmt|;
return|return
name|readNestedProperty
argument_list|(
name|property
argument_list|,
name|pathRemainder
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Object
name|readSimpleProperty
parameter_list|(
name|Object
name|o
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Persistent
condition|)
block|{
name|PropertyDescriptor
name|property
init|=
name|getProperty
argument_list|(
operator|(
name|Persistent
operator|)
name|o
argument_list|,
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
return|return
name|property
operator|.
name|readProperty
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
comment|// handling non-persistent property
return|return
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o
argument_list|,
name|propertyName
argument_list|)
return|;
block|}
comment|/**      * Constructs a dotted path from a list of strings. Useful for creating more      * complex paths while preserving compilation safety. For example, instead      * of saying:      *<p>      *       *<pre>      * orderings.add(new Ordering(&quot;department.name&quot;, SortOrder.ASCENDING));      *</pre>      *<p>      * You can use makePath() with the constants generated by Cayenne Modeler:      *<p>      *       *<pre>      * orderings.add(new Ordering(Cayenne.makePath(USER.DEPARTMENT_PROPERTY, Department.NAME_PROPERTY), SortOrder.ASCENDING));      *</pre>      *<p>      *       * @param pathParts      *            The varargs list of paths to join.      * @return A string of all the paths joined by a "." (used by Cayenne in      *         queries and orderings).      *<p>      * @since 3.1      */
specifier|public
specifier|static
name|String
name|makePath
parameter_list|(
name|String
modifier|...
name|pathParts
parameter_list|)
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|separator
init|=
literal|""
decl_stmt|;
for|for
control|(
name|String
name|path
range|:
name|pathParts
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|separator
argument_list|)
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|separator
operator|=
literal|"."
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns an int primary key value for a persistent object. Only works for      * single column numeric primary keys. If an object is transient or has an      * ObjectId that can not be converted to an int PK, an exception is thrown.      */
specifier|public
specifier|static
name|long
name|longPKForObject
parameter_list|(
name|Persistent
name|dataObject
parameter_list|)
block|{
name|Object
name|value
init|=
name|pkForObject
argument_list|(
name|dataObject
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|value
operator|instanceof
name|Number
operator|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"PK is not a number: %s"
argument_list|,
name|dataObject
operator|.
name|getObjectId
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|(
operator|(
name|Number
operator|)
name|value
operator|)
operator|.
name|longValue
argument_list|()
return|;
block|}
comment|/**      * Returns an int primary key value for a persistent object. Only works for      * single column numeric primary keys. If an object is transient or has an      * ObjectId that can not be converted to an int PK, an exception is thrown.      */
specifier|public
specifier|static
name|int
name|intPKForObject
parameter_list|(
name|Persistent
name|dataObject
parameter_list|)
block|{
name|Object
name|value
init|=
name|pkForObject
argument_list|(
name|dataObject
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|value
operator|instanceof
name|Number
operator|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"PK is not a number: %s"
argument_list|,
name|dataObject
operator|.
name|getObjectId
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|(
operator|(
name|Number
operator|)
name|value
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
comment|/**      * Returns a primary key value for a persistent object. Only works for      * single column primary keys. If an object is transient or has a compound      * ObjectId, an exception is thrown.      */
specifier|public
specifier|static
name|Object
name|pkForObject
parameter_list|(
name|Persistent
name|dataObject
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|pk
init|=
name|extractObjectId
argument_list|(
name|dataObject
argument_list|)
decl_stmt|;
if|if
condition|(
name|pk
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Expected single column PK, got %d columns, ID: %s"
argument_list|,
name|pk
operator|.
name|size
argument_list|()
argument_list|,
name|pk
argument_list|)
throw|;
block|}
return|return
name|pk
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
comment|/**      * Returns a primary key map for a persistent object. This method is the      * most generic out of all methods for primary key retrieval. It will work      * for all possible types of primary keys. If an object is transient, an      * exception is thrown.      */
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|compoundPKForObject
parameter_list|(
name|Persistent
name|dataObject
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|extractObjectId
argument_list|(
name|dataObject
argument_list|)
argument_list|)
return|;
block|}
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|extractObjectId
parameter_list|(
name|Persistent
name|dataObject
parameter_list|)
block|{
if|if
condition|(
name|dataObject
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null DataObject"
argument_list|)
throw|;
block|}
name|ObjectId
name|id
init|=
name|dataObject
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
return|return
name|id
operator|.
name|getIdSnapshot
argument_list|()
return|;
block|}
comment|// replacement ID is more tricky... do some sanity check...
if|if
condition|(
name|id
operator|.
name|isReplacementIdAttached
argument_list|()
condition|)
block|{
name|ObjEntity
name|objEntity
init|=
name|dataObject
operator|.
name|getObjectContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|dataObject
argument_list|)
decl_stmt|;
if|if
condition|(
name|objEntity
operator|!=
literal|null
condition|)
block|{
name|DbEntity
name|entity
init|=
name|objEntity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
operator|&&
name|entity
operator|.
name|isFullReplacementIdAttached
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|id
operator|.
name|getReplacementIdMap
argument_list|()
return|;
block|}
block|}
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't get primary key from temporary id."
argument_list|)
throw|;
block|}
comment|/**      * Returns an object matching an int primary key. If the object is mapped to      * use non-integer PK or a compound PK, CayenneRuntimeException is thrown.      *<p>      * If this object is already cached in the ObjectStore, it is returned      * without a query. Otherwise a query is built and executed against the      * database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|objectForPK
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|dataObjectClass
parameter_list|,
name|int
name|pk
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|objectForPK
argument_list|(
name|context
argument_list|,
name|buildId
argument_list|(
name|context
argument_list|,
name|dataObjectClass
argument_list|,
name|pk
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching an Object primary key. If the object is mapped      * to use a compound PK, CayenneRuntimeException is thrown.      *<p>      * If this object is already cached in the ObjectStore, it is returned      * without a query. Otherwise a query is built and executed against the      * database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|objectForPK
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|dataObjectClass
parameter_list|,
name|Object
name|pk
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|objectForPK
argument_list|(
name|context
argument_list|,
name|buildId
argument_list|(
name|context
argument_list|,
name|dataObjectClass
argument_list|,
name|pk
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching a primary key. PK map parameter should use      * database PK column names as keys.      *<p>      * If this object is already cached in the ObjectStore, it is returned      * without a query. Otherwise a query is built and executed against the      * database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|objectForPK
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|dataObjectClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|pk
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|dataObjectClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Non-existent ObjEntity for class: %s"
argument_list|,
name|dataObjectClass
argument_list|)
throw|;
block|}
return|return
operator|(
name|T
operator|)
name|objectForPK
argument_list|(
name|context
argument_list|,
name|ObjectId
operator|.
name|of
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|pk
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching an int primary key. If the object is mapped to      * use non-integer PK or a compound PK, CayenneRuntimeException is thrown.      *<p>      * If this object is already cached in the ObjectStore, it is returned      * without a query. Otherwise a query is built and executed against the      * database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
specifier|public
specifier|static
name|Object
name|objectForPK
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|String
name|objEntityName
parameter_list|,
name|int
name|pk
parameter_list|)
block|{
return|return
name|objectForPK
argument_list|(
name|context
argument_list|,
name|buildId
argument_list|(
name|context
argument_list|,
name|objEntityName
argument_list|,
name|pk
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching an Object primary key. If the object is mapped      * to use a compound PK, CayenneRuntimeException is thrown.      *<p>      * If this object is already cached in the ObjectStore, it is returned      * without a query. Otherwise a query is built and executed against the      * database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
specifier|public
specifier|static
name|Object
name|objectForPK
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|String
name|objEntityName
parameter_list|,
name|Object
name|pk
parameter_list|)
block|{
return|return
name|objectForPK
argument_list|(
name|context
argument_list|,
name|buildId
argument_list|(
name|context
argument_list|,
name|objEntityName
argument_list|,
name|pk
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching a primary key. PK map parameter should use      * database PK column names as keys.      *<p>      * If this object is already cached in the ObjectStore, it is returned      * without a query. Otherwise a query is built and executed against the      * database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
specifier|public
specifier|static
name|Object
name|objectForPK
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|String
name|objEntityName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|pk
parameter_list|)
block|{
if|if
condition|(
name|objEntityName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null ObjEntity name."
argument_list|)
throw|;
block|}
return|return
name|objectForPK
argument_list|(
name|context
argument_list|,
name|ObjectId
operator|.
name|of
argument_list|(
name|objEntityName
argument_list|,
name|pk
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching ObjectId. If this object is already cached in      * the ObjectStore, it is returned without a query. Otherwise a query is      * built and executed against the database.      *       * @return A persistent object that matched the id, null if no matching      *         objects were found      * @throws CayenneRuntimeException      *             if more than one object matched ObjectId.      */
specifier|public
specifier|static
name|Object
name|objectForPK
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|ObjectId
name|id
parameter_list|)
block|{
return|return
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|id
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an object or a DataRow that is a result of a given query. If      * query returns more than one object, an exception is thrown. If query      * returns no objects, null is returned.      */
specifier|public
specifier|static
name|Object
name|objectForQuery
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
if|else if
condition|(
name|objects
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Expected zero or one object, instead query matched: %d"
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|static
name|ObjectId
name|buildId
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|String
name|objEntityName
parameter_list|,
name|Object
name|pk
parameter_list|)
block|{
if|if
condition|(
name|pk
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null PK"
argument_list|)
throw|;
block|}
if|if
condition|(
name|objEntityName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null ObjEntity name."
argument_list|)
throw|;
block|}
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|objEntityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Non-existent ObjEntity: %s"
argument_list|,
name|objEntityName
argument_list|)
throw|;
block|}
name|Collection
argument_list|<
name|String
argument_list|>
name|pkAttributes
init|=
name|entity
operator|.
name|getPrimaryKeyNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|pkAttributes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"PK contains %d columns, expected 1."
argument_list|,
name|pkAttributes
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|attr
init|=
name|pkAttributes
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|ObjectId
operator|.
name|of
argument_list|(
name|objEntityName
argument_list|,
name|attr
argument_list|,
name|pk
argument_list|)
return|;
block|}
specifier|static
name|ObjectId
name|buildId
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|dataObjectClass
parameter_list|,
name|Object
name|pk
parameter_list|)
block|{
if|if
condition|(
name|pk
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null PK"
argument_list|)
throw|;
block|}
if|if
condition|(
name|dataObjectClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null DataObject class."
argument_list|)
throw|;
block|}
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|dataObjectClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unmapped DataObject Class: %s"
argument_list|,
name|dataObjectClass
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|Collection
argument_list|<
name|String
argument_list|>
name|pkAttributes
init|=
name|entity
operator|.
name|getPrimaryKeyNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|pkAttributes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"PK contains %d columns, expected 1."
argument_list|,
name|pkAttributes
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|attr
init|=
name|pkAttributes
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|ObjectId
operator|.
name|of
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|attr
argument_list|,
name|pk
argument_list|)
return|;
block|}
specifier|protected
name|Cayenne
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

