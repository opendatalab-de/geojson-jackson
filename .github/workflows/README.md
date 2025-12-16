# Workflow Description

Workflows for API resource generation, packaging, and release creation can be found here.\
All types defined here are generated for Python and Java: https://github.com/KI-Cockpit/ai-cockpit-api/tree/main/aicockpit-api\
The result is intended to be used as a type library facilitating interaction with the ai-cockpit API.

## create-release.yml

* Sets up the environments needed for openapi code generation in Java and Python
* Generates Java and Python types for all types defined in OpenAPI spec (see link above)
* Sets the release version and creates a Git tag
* Creates a github release
* Pushes the version change and tag
* Publishes ("deploys") maven package to Maven Central

## How-To Use
Enter a version number into the field. This is required and will be used verbatim.

## pr-build.yml

* The same steps as `create-release.yml` except no changes are made (i.e. no commits nor tags) and no artifacts are deployed.
