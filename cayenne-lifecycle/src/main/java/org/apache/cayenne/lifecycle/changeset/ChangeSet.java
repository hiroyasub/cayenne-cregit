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
name|lifecycle
operator|.
name|changeset
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
name|Persistent
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
name|graph
operator|.
name|GraphDiff
import|;
end_import

begin_comment
comment|/**  * Represents a set of changes to persistent objects corresponding to a certain lifecycle  * stage. The changes are presented in a more usable form compared to the internal Cayenne  * representation as {@link GraphDiff}. One or more changes to the same property of the  * same object are all combined in a single {@link PropertyChange} instance.  *   * @since 3.1  * @deprecated since 4.0 in favour of {@link org.apache.cayenne.lifecycle.postcommit.PostCommitFilter}  *  * @see org.apache.cayenne.lifecycle.changemap.ChangeMap  */
end_comment

begin_interface
annotation|@
name|Deprecated
specifier|public
interface|interface
name|ChangeSet
block|{
specifier|public
specifier|static
specifier|final
name|String
name|OBJECT_ID_PROPERTY_NAME
init|=
literal|"cayenne:objectId"
decl_stmt|;
comment|/**      * Returns a map of changes for a given object in its context, keyed by property name.      * If the object is unchanged, an empty map is returned.      */
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
name|getChanges
parameter_list|(
name|Persistent
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

