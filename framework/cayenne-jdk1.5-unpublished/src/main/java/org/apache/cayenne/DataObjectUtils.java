begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|util
operator|.
name|Cayenne
import|;
end_import

begin_comment
comment|/**  * A collection of utility methods to work with DataObjects.  *<p>  *<i>DataObjects and Primary Keys: All methods that allow to extract primary key values  * or use primary keys to find objects are provided for convenience. Still the author's  * belief is that integer sequential primary keys are meaningless in the object model and  * are pure database artifacts. Therefore relying heavily on direct access to PK provided  * via this class (or other such Cayenne API) is not a clean design practice in many  * cases, and sometimes may actually lead to security issues.</i>  *</p>  *   * @since 1.1  * @deprecated since 3.1 {@link org.apache.cayenne.util.Cayenne} class is used instead  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
specifier|final
class|class
name|DataObjectUtils
block|{
comment|/**      * Returns an int primary key value for a persistent object. Only works for single      * column numeric primary keys. If an object is transient or has an ObjectId that can      * not be converted to an int PK, an exception is thrown.      *       * @since 3.0      */
specifier|public
specifier|static
name|long
name|longPKForObject
parameter_list|(
name|Persistent
name|dataObject
parameter_list|)
block|{
return|return
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|dataObject
argument_list|)
return|;
block|}
comment|/**      * Returns an int primary key value for a persistent object. Only works for single      * column numeric primary keys. If an object is transient or has an ObjectId that can      * not be converted to an int PK, an exception is thrown.      */
specifier|public
specifier|static
name|int
name|intPKForObject
parameter_list|(
name|Persistent
name|dataObject
parameter_list|)
block|{
return|return
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|dataObject
argument_list|)
return|;
block|}
comment|/**      * Returns a primary key value for a persistent object. Only works for single column      * primary keys. If an object is transient or has a compound ObjectId, an exception is      * thrown.      */
specifier|public
specifier|static
name|Object
name|pkForObject
parameter_list|(
name|Persistent
name|dataObject
parameter_list|)
block|{
return|return
name|Cayenne
operator|.
name|pkForObject
argument_list|(
name|dataObject
argument_list|)
return|;
block|}
comment|/**      * Returns a primary key map for a persistent object. This method is the most generic      * out of all methods for primary key retrieval. It will work for all possible types      * of primary keys. If an object is transient, an exception is thrown.      */
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
name|Cayenne
operator|.
name|compoundPKForObject
argument_list|(
name|dataObject
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching an int primary key. If the object is mapped to use      * non-integer PK or a compound PK, CayenneRuntimeException is thrown.      *<p>      * If this object is already cached in the ObjectStore, it is returned without a      * query. Otherwise a query is built and executed against the database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
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
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|dataObjectClass
argument_list|,
name|pk
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching an Object primary key. If the object is mapped to use a      * compound PK, CayenneRuntimeException is thrown.      *<p>      * If this object is already cached in the ObjectStore, it is returned without a      * query. Otherwise a query is built and executed against the database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
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
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|dataObjectClass
argument_list|,
name|pk
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching a primary key. PK map parameter should use database PK      * column names as keys.      *<p>      * If this object is already cached in the ObjectStore, it is returned without a      * query. Otherwise a query is built and executed against the database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
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
return|return
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|dataObjectClass
argument_list|,
name|pk
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching an int primary key. If the object is mapped to use      * non-integer PK or a compound PK, CayenneRuntimeException is thrown.      *<p>      * If this object is already cached in the ObjectStore, it is returned without a      * query. Otherwise a query is built and executed against the database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
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
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|objEntityName
argument_list|,
name|pk
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching an Object primary key. If the object is mapped to use a      * compound PK, CayenneRuntimeException is thrown.      *<p>      * If this object is already cached in the ObjectStore, it is returned without a      * query. Otherwise a query is built and executed against the database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
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
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|objEntityName
argument_list|,
name|pk
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching a primary key. PK map parameter should use database PK      * column names as keys.      *<p>      * If this object is already cached in the ObjectStore, it is returned without a      * query. Otherwise a query is built and executed against the database.      *</p>      *       * @see #objectForPK(ObjectContext, ObjectId)      */
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
return|return
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|objEntityName
argument_list|,
name|pk
argument_list|)
return|;
block|}
comment|/**      * Returns an object matching ObjectId. If this object is already cached in the      * ObjectStore, it is returned without a query. Otherwise a query is built and      * executed against the database.      *       * @return A persistent object that matched the id, null if no matching objects were      *         found      * @throws CayenneRuntimeException if more than one object matched ObjectId.      */
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
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|id
argument_list|)
return|;
block|}
comment|/**      * Returns an object or a DataRow that is a result of a given query. If query returns      * more than one object, an exception is thrown. If query returns no objects, null is      * returned.      *       * @since 1.2      */
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
return|return
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
return|;
block|}
comment|// not intended for instantiation
specifier|private
name|DataObjectUtils
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

