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
comment|/**  * A listener registered for notification when an entry is evicted. An instance may be  * called concurrently by multiple threads to process entries. An implementation should  * avoid performing blocking calls or synchronizing on shared resources.  *<p>  * The listener is invoked by {@link ConcurrentLinkedHashMap} on a caller's thread and  * will not block other threads from operating on the map. An implementation should be  * aware that the caller's thread will not expect long execution times or failures as a  * side effect of the listener being notified. Execution safety and a fast turn around  * time can be achieved by performing the operation asynchronously, such as by submitting  * a task to an {@link java.util.concurrent.ExecutorService}.  */
end_comment

begin_interface
interface|interface
name|EvictionListener
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/**      * A call-back notification that the entry was evicted.      *       * @param key the entry's key      * @param value the entry's value      */
name|void
name|onEviction
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

