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
operator|.
name|query
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|CayenneRuntimeException
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
name|ObjectId
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|EntityResolver
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * A query that matches zero or one object or data row corresponding to the ObjectId. Used  * internally by Cayenne to lookup objects by id. Notice that cache policies of  * ObjectIdQuery are different from generic {@link QueryMetadata} cache policies.  * ObjectIdQuery is special - it is the only query that can be done against Cayenne main  * cache, thus cache handling is singnificantly different from all other of the queries.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ObjectIdQuery
extends|extends
name|IndirectQuery
block|{
comment|// TODO: Andrus, 2/18/2006 - reconcile this with QueryMetadata cache policies
specifier|public
specifier|static
specifier|final
name|int
name|CACHE
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|CACHE_REFRESH
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|CACHE_NOREFRESH
init|=
literal|3
decl_stmt|;
specifier|protected
name|ObjectId
name|objectId
decl_stmt|;
specifier|protected
name|int
name|cachePolicy
decl_stmt|;
specifier|protected
name|boolean
name|fetchingDataRows
decl_stmt|;
specifier|protected
specifier|transient
name|EntityResolver
name|metadataResolver
decl_stmt|;
specifier|protected
specifier|transient
name|QueryMetadata
name|metadata
decl_stmt|;
comment|// needed for hessian serialization
specifier|private
name|ObjectIdQuery
parameter_list|()
block|{
name|this
operator|.
name|cachePolicy
operator|=
name|CACHE_REFRESH
expr_stmt|;
block|}
comment|/**      * Creates a refreshing SingleObjectQuery.      */
specifier|public
name|ObjectIdQuery
parameter_list|(
name|ObjectId
name|objectID
parameter_list|)
block|{
name|this
argument_list|(
name|objectID
argument_list|,
literal|false
argument_list|,
name|CACHE_REFRESH
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new ObjectIdQuery.      */
specifier|public
name|ObjectIdQuery
parameter_list|(
name|ObjectId
name|objectId
parameter_list|,
name|boolean
name|fetchingDataRows
parameter_list|,
name|int
name|cachePolicy
parameter_list|)
block|{
if|if
condition|(
name|objectId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null objectID"
argument_list|)
throw|;
block|}
name|this
operator|.
name|objectId
operator|=
name|objectId
expr_stmt|;
name|this
operator|.
name|cachePolicy
operator|=
name|cachePolicy
expr_stmt|;
name|this
operator|.
name|fetchingDataRows
operator|=
name|fetchingDataRows
expr_stmt|;
block|}
comment|/**      * Returns query metadata object.      */
comment|// return metadata without creating replacement, as it is not always possible to
comment|// create replacement (e.g. temp ObjectId).
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
specifier|final
name|EntityResolver
name|resolver
parameter_list|)
block|{
comment|// caching metadata as it may be accessed multiple times (at a DC and DD level)
if|if
condition|(
name|metadata
operator|==
literal|null
operator|||
name|metadataResolver
operator|!=
name|resolver
condition|)
block|{
name|this
operator|.
name|metadata
operator|=
operator|new
name|DefaultQueryMetadata
argument_list|()
block|{
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|objectId
operator|.
name|getEntityName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|getClassDescriptor
argument_list|()
operator|.
name|getEntity
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|fetchingDataRows
return|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|metadataResolver
operator|=
name|resolver
expr_stmt|;
block|}
return|return
name|metadata
return|;
block|}
specifier|public
name|ObjectId
name|getObjectId
parameter_list|()
block|{
return|return
name|objectId
return|;
block|}
specifier|protected
name|Query
name|createReplacementQuery
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|objectId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't resolve query - objectId is null."
argument_list|)
throw|;
block|}
if|if
condition|(
name|objectId
operator|.
name|isTemporary
argument_list|()
operator|&&
operator|!
name|objectId
operator|.
name|isReplacementIdAttached
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't build a query for temporary id: "
operator|+
name|objectId
argument_list|)
throw|;
block|}
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|objectId
operator|.
name|getEntityName
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|matchAllDbExp
argument_list|(
name|objectId
operator|.
name|getIdSnapshot
argument_list|()
argument_list|,
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
argument_list|)
decl_stmt|;
comment|// if we got to the point of fetch, always force refresh....
name|query
operator|.
name|setRefreshingObjects
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|query
operator|.
name|setFetchingDataRows
argument_list|(
name|fetchingDataRows
argument_list|)
expr_stmt|;
return|return
name|query
return|;
block|}
specifier|public
name|int
name|getCachePolicy
parameter_list|()
block|{
return|return
name|cachePolicy
return|;
block|}
specifier|public
name|boolean
name|isFetchMandatory
parameter_list|()
block|{
return|return
name|cachePolicy
operator|==
name|CACHE_REFRESH
return|;
block|}
specifier|public
name|boolean
name|isFetchAllowed
parameter_list|()
block|{
return|return
name|cachePolicy
operator|!=
name|CACHE_NOREFRESH
return|;
block|}
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|fetchingDataRows
return|;
block|}
comment|/**      * Overrides toString() outputting a short string with query class and ObjectId.      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"."
argument_list|)
operator|+
literal|":"
operator|+
name|objectId
return|;
block|}
comment|/**      * An object is considered equal to this query if it is also a SingleObjectQuery with      * an equal ObjectId.      */
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|object
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|ObjectIdQuery
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ObjectIdQuery
name|query
init|=
operator|(
name|ObjectIdQuery
operator|)
name|object
decl_stmt|;
return|return
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|objectId
argument_list|,
name|query
operator|.
name|getObjectId
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Implements a standard hashCode contract considering custom 'equals' implementation.      */
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
operator|(
name|objectId
operator|!=
literal|null
operator|)
condition|?
name|objectId
operator|.
name|hashCode
argument_list|()
else|:
literal|11
return|;
block|}
block|}
end_class

end_unit

