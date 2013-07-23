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

begin_comment
comment|/**  * Defines a set of object states from the point of view of persistence. I.e.  * PersistenceState is the state of data stored in an object relative to the external  * persistence store. If an object's state matches the state of the persistence store, the  * object is COMMITTED. If object is not intended to be persistent or is not explicitly  * made persistent, the state is TRANSIENT, and so on.  *<p>  * Object persistence states should not be modified directly. Rather it is a  * responsibility of a ObjectContext/DataContext to maintain correct state of the managed  * objects.  *   */
end_comment

begin_class
specifier|public
class|class
name|PersistenceState
block|{
comment|/**      * Returns String label for persistence state. Used for debugging.      */
specifier|public
specifier|static
name|String
name|persistenceStateName
parameter_list|(
name|int
name|persistenceState
parameter_list|)
block|{
switch|switch
condition|(
name|persistenceState
condition|)
block|{
case|case
name|PersistenceState
operator|.
name|TRANSIENT
case|:
return|return
literal|"transient"
return|;
case|case
name|PersistenceState
operator|.
name|NEW
case|:
return|return
literal|"new"
return|;
case|case
name|PersistenceState
operator|.
name|MODIFIED
case|:
return|return
literal|"modified"
return|;
case|case
name|PersistenceState
operator|.
name|COMMITTED
case|:
return|return
literal|"committed"
return|;
case|case
name|PersistenceState
operator|.
name|HOLLOW
case|:
return|return
literal|"hollow"
return|;
case|case
name|PersistenceState
operator|.
name|DELETED
case|:
return|return
literal|"deleted"
return|;
default|default:
return|return
literal|"unknown"
return|;
block|}
block|}
comment|/**      * Describes a state of an object not registered with DataContext/ObjectContext, and      * therefore having no persistence features.      */
specifier|public
specifier|static
specifier|final
name|int
name|TRANSIENT
init|=
literal|1
decl_stmt|;
comment|/**      * Describes a state of an object freshly registered with DataContext/ObjectContext,      * but not committed to the database yet. So there is no corresponding database record      * for this object just yet.      */
specifier|public
specifier|static
specifier|final
name|int
name|NEW
init|=
literal|2
decl_stmt|;
comment|/**      * Describes a state of an object registered with DataContext/ObjectContext, whose      * fields exactly match the state of a corresponding database row. This state is not      * fully "clean", since database record may have been externally modified.      */
specifier|public
specifier|static
specifier|final
name|int
name|COMMITTED
init|=
literal|3
decl_stmt|;
comment|/**      * Describes a state of an object registered with DataContext/ObjectContext, and      * having a corresponding database row. This object state is known to be locally      * modified and different from the database state.      */
specifier|public
specifier|static
specifier|final
name|int
name|MODIFIED
init|=
literal|4
decl_stmt|;
comment|/**      * Describes a state of an object registered with DataContext/ObjectContext, and      * having a corresponding database row. This object does not store any fields except      * for its id (it is "hollow"), so next time it is accessed, it will be populated from      * the database by the context. In this respect this is a real "clean" object.      */
specifier|public
specifier|static
specifier|final
name|int
name|HOLLOW
init|=
literal|5
decl_stmt|;
comment|/**      * Describes a state of an object registered with DataContext/ObjectContext, that will      * be deleted from the database on the next commit.      */
specifier|public
specifier|static
specifier|final
name|int
name|DELETED
init|=
literal|6
decl_stmt|;
block|}
end_class

end_unit
