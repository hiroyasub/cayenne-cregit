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
name|instrument
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|instrument
operator|.
name|Instrumentation
import|;
end_import

begin_comment
comment|/**  * An agent that provides access to {@link Instrumentation} instance  *<p>  * To enable CayenneAgent (and hence class enhancers in the Java SE environment), start  * the JVM with the "-javaagent:" option. E.g.:  *   *<pre>java -javaagent:/path/to/cayenne-agent-xxxx.jar org.example.Main</pre>  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CayenneAgent
block|{
specifier|static
name|Instrumentation
name|instrumentation
decl_stmt|;
specifier|public
specifier|static
name|void
name|premain
parameter_list|(
name|String
name|agentArgs
parameter_list|,
name|Instrumentation
name|instrumentation
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"*** CayenneAgent starting..."
argument_list|)
expr_stmt|;
name|CayenneAgent
operator|.
name|instrumentation
operator|=
name|instrumentation
expr_stmt|;
block|}
specifier|public
specifier|static
name|Instrumentation
name|getInstrumentation
parameter_list|()
block|{
return|return
name|instrumentation
return|;
block|}
block|}
end_class

end_unit

