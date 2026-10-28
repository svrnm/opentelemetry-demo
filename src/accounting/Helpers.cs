// Copyright The OpenTelemetry Authors
// SPDX-License-Identifier: Apache-2.0

using System.Collections;

namespace Accounting
{
    internal static class Helpers
    {
        private static List<string> RelevantPrefixes = ["DOTNET_", "CORECLR_", "OTEL_", "KAFKA_"];

        // Values of these variables are masked when printed, e.g. KAFKA_SASL_PASSWORD.
        private static List<string> SensitiveKeyParts = ["PASSWORD"];

        public static IEnumerable<DictionaryEntry> FilterRelevant(this IDictionary envs)
        {
            foreach (DictionaryEntry env in envs)
            {
                foreach (var prefix in RelevantPrefixes)
                {
                    if (env.Key.ToString()?.StartsWith(prefix, StringComparison.InvariantCultureIgnoreCase) ?? false)
                    {
                        yield return env;
                    }
                }
            }
        }

        public static void OutputInOrder(this IEnumerable<DictionaryEntry> envs)
        {
            foreach (var env in envs.OrderBy(x => x.Key))
            {
                var key = env.Key.ToString() ?? string.Empty;
                var sensitive = SensitiveKeyParts.Any(part => key.Contains(part, StringComparison.InvariantCultureIgnoreCase));
                Console.WriteLine(sensitive ? new DictionaryEntry(env.Key, "***") : env);
            }
        }
    }
}
