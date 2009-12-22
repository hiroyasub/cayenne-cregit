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
name|pref
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|pref
operator|.
name|DBConnectionInfo
import|;
end_import

begin_class
specifier|public
class|class
name|CayenneProjectPreferences
block|{
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
name|cayenneProjectPreferences
decl_stmt|;
specifier|public
name|CayenneProjectPreferences
parameter_list|()
block|{
name|cayenneProjectPreferences
operator|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|cayenneProjectPreferences
operator|.
name|put
argument_list|(
name|DBConnectionInfo
operator|.
name|class
argument_list|,
operator|new
name|ChildrenMapPreference
argument_list|(
operator|new
name|DBConnectionInfo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|initPreference
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initPreference
parameter_list|()
block|{
name|Iterator
name|it
init|=
name|cayenneProjectPreferences
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
name|pairs
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
operator|(
operator|(
name|ChildrenMapPreference
operator|)
name|cayenneProjectPreferences
operator|.
name|get
argument_list|(
name|pairs
operator|.
name|getKey
argument_list|()
argument_list|)
operator|)
operator|.
name|initChildrenPreferences
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|ChildrenMapPreference
name|getDetailObject
parameter_list|(
name|Class
name|className
parameter_list|)
block|{
return|return
operator|(
name|ChildrenMapPreference
operator|)
name|cayenneProjectPreferences
operator|.
name|get
argument_list|(
name|className
argument_list|)
return|;
block|}
block|}
end_class

end_unit

