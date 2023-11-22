---
title: Jikkou
linkTitle: Jikkou
---
{{% blocks/cover title="Jikkou (jikkō / 実行)!" image_anchor="top" height="max" color="white" %}}
<div class="mx-auto">
	<h2 class="mb-5">Efficient and easy automation and provisioning of resources for any Kafka infrastructure.</h2>
	<a class="btn btn-lg btn-secondary mr-3 mb-4" href="{{< relref "/docs" >}}">
	Learn More <i class="fas fa-arrow-alt-circle-right ml-2"></i>
	</a>
	<a class="btn btn-lg btn-github mr-3 mb-4" href="https://github.com/streamthoughts/jikkou">
		Download <i class="fab fa-github ml-2 "></i>
	</a>
</div>
{{% /blocks/cover %}}

{{% blocks/lead color="white" %}}

[<img src="./images/jikkou-no-title-logo-450px.png" width="100px"/>](./images/jikkou-no-title-logo-450px.png)

**[Jikkou](https://github.com/streamthoughts/jikkou)** (**jikkō / 実行**) is an open-source product designed to
provide an efficient and easy way to manage, automate and provision resources on your self-service Event-Driven
[Data Mesh](https://martinfowler.com/articles/data-mesh-principles.html) platforms (or, more simply, on any Apache Kafka Infrastructures).

Developed by Kafka ❤️, Jikkou aims to streamline daily operations on [Apache Kafka](https://kafka.apache.org/documentation/),
ensuring that platform governance is no longer a boring and tedious task for both **Developers** and **Administrators**.

Jikkou enables a declarative management approach of **Topics**, **ACLs**, **Quotas**, **Schemas**, **Connectors** and
even more with the use of YAML files called **_Resource Definitions_**.

Taking inspiration from `kubectl` and Kubernetes resource definition files, Jikkou offers an intuitive and user-friendly approach to configuration management.

Jikkou can be used with [Apache Kafka](https://kafka.apache.org/), [Aiven](https://aiven.io/kafka), [Amazon MSK](https://aws.amazon.com/fr/msk/), [Confluent Cloud](https://www.confluent.io/confluent-cloud/), [Redpanda](https://redpanda.com/).

Jikkou is distributed under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0). Apache, Apache Kafka, Kafka, and associated open source project names are trademarks of the Apache Software Foundation.

{{% /blocks/lead %}}

{{% blocks/section type="row" color="dark"%}}

{{% blocks/feature icon="fab fa-github" title="Contributions welcome" %}}
Want to join the fun on Github? New users are always welcome!

<a class="text-white" href="docs/contribution-guidelines/">
	<button type="button" class="btn btn-github" style="width:150px; margin-top: 12px;">Contribute</button>
</a>
{{% /blocks/feature %}}

{{% blocks/feature icon="fas"  %}}
{{% /blocks/feature %}}

{{% blocks/feature icon="fas fa-star" title="Support Jikkou Team" %}}
Add a star to the GitHub project, it only takes 5 seconds!

<a class="text-white" href="https://github.com/streamthoughts/jikkou">
	<button type="button" class="btn btn-github" style="width:150px; margin-top: 12px;">Star</button>
</a>
{{% /blocks/feature %}}
{{% /blocks/section %}}