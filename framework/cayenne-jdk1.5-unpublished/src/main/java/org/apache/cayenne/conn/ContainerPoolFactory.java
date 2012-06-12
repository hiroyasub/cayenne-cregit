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
name|conn
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Name
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|RefAddr
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|spi
operator|.
name|ObjectFactory
import|;
end_import

begin_comment
comment|/**  *<p>  * Basic JNDI object factory that creates an instance of<code>PoolManager</code> that has  * been configured based on the<code>RefAddr</code> values of the specified  *<code>Reference</code>.  *</p>  *<p>  * Here is a sample Tomcat 4.x configuration that sets this class as a default factory for  * javax.sql.DataSource objects:  *</p>  *<code><pre>&lt;ResourceParams name="jdbc/mydb"&gt;&lt;parameter&gt;&lt;name&gt;factory&lt;/name&gt;&lt;value>org.apache.cayenne.conn.ContainerPoolFactory&lt;/value&gt;&lt;/parameter&gt;&lt;parameter>&lt;name>username&lt;/name>&lt;value>andrei&lt;/value>&lt;/parameter>&lt;parameter>&lt;name>password&lt;/name>&lt;value>bla-bla&lt;/value>&lt;/parameter>&lt;parameter>&lt;name>driver&lt;/name>&lt;value>org.gjt.mm.mysql.Driver&lt;/value>&lt;/parameter>&lt;parameter>&lt;name>url&lt;/name>&lt;value>jdbc:mysql://noise/cayenne&lt;/value>&lt;/parameter>&lt;parameter>&lt;name>min&lt;/name>&lt;value>1&lt;/value>&lt;/parameter>&lt;parameter>&lt;name>max&lt;/name>&lt;value>3&lt;/value>&lt;/parameter>&lt;/ResourceParams></pre></code>  *<p>  * After ContainerPoolFactory was configured to be used within the container (see above  * for Tomcat example), you can reference your "jdbc/mydb" DataSource in web application  * deployment descriptor like that (per Servlet Specification):  *</p>  *<code><pre>&lt;resource-ref>&lt;es-ref-name>jdbc/mydb&lt;/res-ref-name>&lt;res-type>javax.sql.DataSource&lt;/res-type>&lt;res-auth>Container&lt;/res-auth>&lt;/resource-ref></pre></code>  *   * @deprecated since 3.1. This class does not belong in Cayenne, as Cayenne no longer  *             attempts to provide appserver pieces. End users should not need this class  *             and should use their container's preferred approach to map a DataSource  *             instead.  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|ContainerPoolFactory
implements|implements
name|ObjectFactory
block|{
comment|/**      *<p>      * Creates and returns a new<code>PoolManager</code> instance. If no instance can be      * created, returns<code>null</code> instead.      *</p>      *       * @param obj The possibly null object containing location or reference information      *            that can be used in creating an object      * @param name The name of this object relative to<code>nameCtx</code>      * @param nameCtx The context relative to which the<code>name</code> parameter is      *            specified, or<code>null</code> if<code>name</code> is relative to the      *            default initial context      * @param environment The possibly null environment that is used in creating this      *            object      * @exception Exception if an exception occurs creating the instance      */
specifier|public
name|Object
name|getObjectInstance
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Name
name|name
parameter_list|,
name|Context
name|nameCtx
parameter_list|,
name|Hashtable
name|environment
parameter_list|)
throws|throws
name|Exception
block|{
comment|// We only know how to deal with<code>javax.naming.Reference</code>s
comment|// that specify a class name of "javax.sql.DataSource"
if|if
condition|(
operator|(
name|obj
operator|==
literal|null
operator|)
operator|||
operator|!
operator|(
name|obj
operator|instanceof
name|Reference
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Reference
name|ref
init|=
operator|(
name|Reference
operator|)
name|obj
decl_stmt|;
if|if
condition|(
operator|!
literal|"javax.sql.DataSource"
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Create and configure a PoolManager instance based on the
comment|// RefAddr values associated with this Reference
name|RefAddr
name|ra
init|=
literal|null
decl_stmt|;
name|String
name|driver
init|=
literal|null
decl_stmt|;
name|String
name|url
init|=
literal|null
decl_stmt|;
name|int
name|min
init|=
literal|1
decl_stmt|;
name|int
name|max
init|=
literal|1
decl_stmt|;
name|String
name|username
init|=
literal|null
decl_stmt|;
name|String
name|password
init|=
literal|null
decl_stmt|;
name|ra
operator|=
name|ref
operator|.
name|get
argument_list|(
literal|"min"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ra
operator|!=
literal|null
condition|)
block|{
name|min
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|ra
operator|.
name|getContent
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ra
operator|=
name|ref
operator|.
name|get
argument_list|(
literal|"max"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ra
operator|!=
literal|null
condition|)
block|{
name|max
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|ra
operator|.
name|getContent
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ra
operator|=
name|ref
operator|.
name|get
argument_list|(
literal|"driver"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ra
operator|!=
literal|null
condition|)
block|{
name|driver
operator|=
name|ra
operator|.
name|getContent
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|ra
operator|=
name|ref
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ra
operator|!=
literal|null
condition|)
block|{
name|password
operator|=
name|ra
operator|.
name|getContent
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|ra
operator|=
name|ref
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ra
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
name|ra
operator|.
name|getContent
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|ra
operator|=
name|ref
operator|.
name|get
argument_list|(
literal|"username"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ra
operator|!=
literal|null
condition|)
block|{
name|username
operator|=
name|ra
operator|.
name|getContent
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|PoolManager
argument_list|(
name|driver
argument_list|,
name|url
argument_list|,
name|min
argument_list|,
name|max
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
block|}
end_class

end_unit

