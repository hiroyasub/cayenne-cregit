begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_comment
comment|/*  * Copyright 2010 Google Inc. All Rights Reserved.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     https://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|concurrentlinkedhashmap
package|;
end_package

begin_comment
comment|/**  * A class that can determine the weight of a value. The total weight threshold is used to  * determine when an eviction is required.  */
end_comment

begin_interface
interface|interface
name|Weigher
parameter_list|<
name|V
parameter_list|>
block|{
comment|/**      * Measures an object's weight to determine how many units of capacity that the value      * consumes. A value must consume a minimum of one unit.      *       * @param value the object to weigh      * @return the object's weight      */
name|int
name|weightOf
parameter_list|(
name|V
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

