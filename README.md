# RDAP client library for Java

## Bootstrapping and Redirectors
This library supports bootstrapping via the IANA bootstrap registries as defined in RFC 9224, but a redirector (as defined in RFC 7480 Appendix C) can be used by setting it as the service URL when creating an RDAPClient instance.

## Compatibility
This client library implements the following standards:
- [RFC 7480 (RDAP over HTTP)](https://datatracker.ietf.org/doc/html/rfc7480)
- [RFC 8056 (RDAP-EPP status mapping) (WIP)](https://datatracker.ietf.org/doc/html/rfc8056)
- [RFC 8605 (RDAP vCard extensions) (WIP)](https://datatracker.ietf.org/doc/html/rfc8605)
- [RFC 9082 (RDAP query format) (WIP)](https://datatracker.ietf.org/doc/html/rfc9082)
- [RFC 9083 (RDAP JSON responses) (WIP)](https://datatracker.ietf.org/doc/html/rfc9083)
- [RFC 9224 (RDAP server discovery)](https://datatracker.ietf.org/doc/html/rfc9224)

## Known Limitations
- The client library does not implement retries in case of rate limiting (RFC 7480 5.5) because correct handling will depend on the application

## Planned Additions
- Support for commonly used extensions (see https://www.iana.org/assignments/rdap-extensions/rdap-extensions.xhtml)
- Detection of excessive caches and circumvention of those (see RFC 7480 Appendix B)