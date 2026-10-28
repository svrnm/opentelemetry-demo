// Copyright The OpenTelemetry Authors
// SPDX-License-Identifier: Apache-2.0
package kafka

import "github.com/xdg-go/scram"

// scramClient implements sarama.SCRAMClient, which sarama needs for the SCRAM-SHA-* mechanisms.
// Based on https://github.com/IBM/sarama/blob/main/examples/sasl_scram_client/scram_client.go
type scramClient struct {
	*scram.Client
	*scram.ClientConversation
	scram.HashGeneratorFcn
}

func (x *scramClient) Begin(userName, password, authzID string) (err error) {
	x.Client, err = x.HashGeneratorFcn.NewClient(userName, password, authzID)
	if err != nil {
		return err
	}
	x.ClientConversation = x.Client.NewConversation()
	return nil
}

func (x *scramClient) Step(challenge string) (string, error) {
	return x.ClientConversation.Step(challenge)
}

func (x *scramClient) Done() bool {
	return x.ClientConversation.Done()
}
