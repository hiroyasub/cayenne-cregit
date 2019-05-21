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
operator|.
name|configuration
operator|.
name|xml
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
name|configuration
operator|.
name|ConfigurationNode
import|;
end_import

begin_comment
comment|/**  *<p>  * Storage for all kind of meta data that is not required for runtime.  *</p>  *<p>  * Currently used by Modeler and cli tools (e.g. Maven, Ant and Gradle) to store project extra data.  *</p>  *  *<p>  * Usage:<pre>  *      // attach custom information to data map  *      metaData.add(dataMap, myObject);  *  *      // read data  *      MyObject obj = metaData.get(dataMap, MyObject.class);  *</pre>  *</p>  *  * @since 4.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataChannelMetaData
block|{
comment|/**      * Store data for object.      *      * @param key object for which we want to store data      * @param value data to store      */
name|void
name|add
parameter_list|(
name|ConfigurationNode
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      *      * Get meta data for object      *      * @param key object for wich we want meta data      * @param type meta data type class      * @param<T> meta data type      * @return value or {@code null} if no data available      */
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|ConfigurationNode
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * @since 4.1      * @param key object for wich we want meta data      * @param type meta data type class      * @param<T> meta data type      * @return value or {@code null} if no can't remove      */
parameter_list|<
name|T
parameter_list|>
name|T
name|remove
parameter_list|(
name|ConfigurationNode
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

